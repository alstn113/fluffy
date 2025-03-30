package com.fluffy.support

import com.fluffy.support.cleaner.DataCleanupExtension
import com.fluffy.support.testcontainers.PostgreSQLContainerInitializer
import com.fluffy.support.testcontainers.RedisContainerInitializer
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestConstructor


@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(
    initializers = [
        RedisContainerInitializer::class,
        PostgreSQLContainerInitializer::class
    ]
)
@ExtendWith(DataCleanupExtension::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class AbstractIntegrationTest
