package com.fluffy.support;

import com.fluffy.support.cleaner.DatabaseCleaner;
import com.fluffy.support.cleaner.DatabaseClearExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {AbstractIntegrationTest.TestConfig.class})
@ExtendWith(DatabaseClearExtension.class)
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    @SpyBean
    protected RedissonClient redissonClient;

    @TestConfiguration
    public static class TestConfig {

        @Bean
        public DatabaseCleaner databaseCleaner() {
            return new DatabaseCleaner();
        }
    }
}
