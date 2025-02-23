package com.fluffy.support.cleaner;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RedisDataCleaner implements DataCleaner {

    private static final Logger log = LoggerFactory.getLogger(RedisDataCleaner.class);

    @Autowired
    private RedisTemplate<?, ?> redisTemplate;

    @Override
    @Transactional
    public void clear() {
        clearCache();
    }

    private void clearCache() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory())
                .getConnection()
                .serverCommands()
                .flushDb();

        log.info("[RedisDataCleaner] Redis cache is cleared.");
    }
}
