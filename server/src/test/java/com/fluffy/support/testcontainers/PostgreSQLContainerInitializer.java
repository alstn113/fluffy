package com.fluffy.support.testcontainers;

import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgreSQLContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:16-alpine");

    static {
        POSTGRESQL.start();
    }

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext context) {
        Map<String, String> properties = Map.of(
                "spring.datasource.url", POSTGRESQL.getJdbcUrl(),
                "spring.datasource.username", POSTGRESQL.getUsername(),
                "spring.datasource.password", POSTGRESQL.getPassword()
        );

        TestPropertyValues.of(properties).applyTo(context);
    }
}
