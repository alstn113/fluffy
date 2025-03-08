package com.fluffy.support.testcontainers;

import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;

public class RedisContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final GenericContainer<?> REDIS = new GenericContainer<>("valkey/valkey:8-alpine")
            .withExposedPorts(6379);

    static {
        REDIS.start();
    }

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext context) {
        Map<String, String> properties = Map.of(
                "spring.data.redis.host", REDIS.getHost(),
                "spring.data.redis.port", String.valueOf(REDIS.getFirstMappedPort()),
                "spring.data.redis.ssl.enabled", "false"
        );

        TestPropertyValues.of(properties).applyTo(context);
    }
}