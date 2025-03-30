package com.fluffy.support.cleaner

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class RedisCacheCleaner(
    private val redisTemplate: RedisTemplate<String, String>
) : DataCleaner {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(RedisCacheCleaner::class.java)
    }

    override fun clear() {
        clearCache()
    }

    fun clearCache() {
        redisTemplate.connectionFactory!!
            .connection
            .serverCommands()
            .flushDb()

        log.info("[RedisCacheCleaner] Cache is cleared.")
    }
}