package com.fluffy.infra.redis

import com.fluffy.global.distributedLock.DistributedLock
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class DistributedLockAspect(
    private val redissonClient: RedissonClient,
    private val aopForTransaction: AopForTransaction
) {

    companion object {
        private val log = LoggerFactory.getLogger(DistributedLockAspect::class.java)
        private const val LOCK_PREFIX = "lock:"
    }

    @Around("@annotation(distributedLock)")
    fun lock(joinPoint: ProceedingJoinPoint, distributedLock: DistributedLock): Any? {
        val signature = joinPoint.signature as MethodSignature
        val key = LOCK_PREFIX + CustomSpringELParser.getDynamicValue(
            signature.parameterNames,
            joinPoint.args,
            distributedLock.key
        )

        val rLock = redissonClient.getLock(key)
        try {
            val isLocked = rLock.tryLock(
                distributedLock.waitTime,
                distributedLock.leaseTime,
                distributedLock.timeUnit
            )
            if (!isLocked) {
                return false
            }
            return aopForTransaction.proceed(joinPoint)
        } finally {
            try {
                rLock.unlock()
            } catch (e: Exception) {
                log.info("[DistributedLock] Failed to release lock for key: {}", key, e)
            }
        }
    }
}
