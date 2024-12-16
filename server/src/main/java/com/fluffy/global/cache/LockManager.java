package com.fluffy.global.cache;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LockManager {

    private final RedisTemplate<String, String> redisTemplate;

    public Boolean lock(String key) {
        return redisTemplate.opsForValue().setIfAbsent(key, "lock", Duration.ofSeconds(3));
    }

    public Boolean unlock(String key) {
        return redisTemplate.delete(key);
    }
}
