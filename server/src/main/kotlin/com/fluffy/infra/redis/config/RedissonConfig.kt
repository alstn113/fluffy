package com.fluffy.infra.redis.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedissonConfig(
    private val properties: RedisProperties,
) {

    companion object {
        private const val REDIS_URL = "redis://%s:%d"
        private const val REDIS_SSL_URL = "rediss://%s:%d"
    }

    @Bean
    fun redissonClient(): RedissonClient {
        val config = Config()
        config.useSingleServer()
            .setAddress(createUrl().format(properties.host, properties.port))
            .setSslEnableEndpointIdentification(properties.ssl.enabled)

        return Redisson.create(config)
    }

    private fun createUrl(): String {
        if (properties.ssl.enabled) {
            return REDIS_SSL_URL
        }
        return REDIS_URL
    }
}
