package com.fluffy.support;

import com.fluffy.infra.storage.StorageClient;
import com.fluffy.support.cleaner.DataCleanupExtension;
import com.fluffy.support.testcontainers.PostgreSQLContainerInitializer;
import com.fluffy.support.testcontainers.RedisContainerInitializer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ExtendWith(DataCleanupExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(initializers = {
        RedisContainerInitializer.class,
        PostgreSQLContainerInitializer.class
})
public abstract class AbstractIntegrationTest {

    @MockBean
    protected StorageClient storageClient;
}
