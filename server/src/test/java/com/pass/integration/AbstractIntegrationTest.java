package com.pass.integration;

import com.pass.support.cleaner.DatabaseCleaner;
import com.pass.support.cleaner.DatabaseClearExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {AbstractIntegrationTest.TestConfig.class})
@ExtendWith(DatabaseClearExtension.class)
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    @TestConfiguration
    public static class TestConfig {

        @Bean
        public DatabaseCleaner databaseCleaner() {
            return new DatabaseCleaner();
        }
    }
}
