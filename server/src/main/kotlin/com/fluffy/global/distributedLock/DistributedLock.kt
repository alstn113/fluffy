package com.fluffy.global.distributedLock

import java.util.concurrent.TimeUnit

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DistributedLock(

    val key: String,

    val timeUnit: TimeUnit = TimeUnit.SECONDS,

    /**
     * 락을 획득하기 위해 대기할 시간
     */
    val waitTime: Long = 3L,

    /**
     * 락을 유지할 시간
     */
    val leaseTime: Long = 3L
)
