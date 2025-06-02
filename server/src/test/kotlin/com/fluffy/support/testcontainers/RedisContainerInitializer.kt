package com.fluffy.support.testcontainers

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.GenericContainer

class RedisContainerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    companion object {
        private val REDIS = GenericContainer("valkey/valkey:8.0.1-alpine")
            .withExposedPorts(6379)

        init {
            REDIS.start()
        }
    }

    override fun initialize(context: ConfigurableApplicationContext) {
        val properties: Map<String, String> = mapOf(
            "spring.data.redis.host" to REDIS.host,
            "spring.data.redis.port" to REDIS.firstMappedPort.toString(),
            "spring.data.redis.ssl.enabled" to false.toString()
        )

        TestPropertyValues.of(properties).applyTo(context)
    }
}