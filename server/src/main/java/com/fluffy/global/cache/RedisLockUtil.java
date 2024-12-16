package com.fluffy.global.cache;

import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisLockUtil {

    private final LockManager manager;

    public <T> T acquireAndRunLock(String key, Supplier<T> block) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("[RedisLock] Key cannot be null or empty");
        }

        if (Boolean.TRUE.equals(acquireLock(key))) {
            return proceedWithLock(key, block);
        }

        throw new IllegalStateException("[RedisLock] Failed to acquire lock for key: " + key);
    }

    private Boolean acquireLock(String key) {
        try {
            return manager.lock(key);
        } catch (Exception e) {
            log.error("[RedisLock] Failed to acquire lock for key: {}", key, e);
            return false;
        }
    }

    private <T> T proceedWithLock(String key, Supplier<T> block) {
        try {
            return block.get();
        } finally {
            releaseLock(key);
        }
    }

    private void releaseLock(String key) {
        try {
            manager.unlock(key);
        } catch (Exception e) {
            log.error("[RedisLock] Failed to release lock for key: {}", key, e);
        }
    }
}
