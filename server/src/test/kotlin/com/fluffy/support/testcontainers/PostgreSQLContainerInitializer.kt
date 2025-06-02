package com.fluffy.support.testcontainers

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer

class PostgreSQLContainerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    companion object {
        private val POSTGRESQL = PostgreSQLContainer("postgres:16.3-alpine")

        init {
            POSTGRESQL.start()
        }
    }

    override fun initialize(context: ConfigurableApplicationContext) {
        val properties = mapOf(
            "spring.datasource.url" to POSTGRESQL.jdbcUrl,
            "spring.datasource.username" to POSTGRESQL.username,
            "spring.datasource.password" to POSTGRESQL.password
        )

        TestPropertyValues.of(properties).applyTo(context)
    }
}