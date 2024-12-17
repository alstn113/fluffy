package com.fluffy.global.redis;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties properties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(properties.host(), properties.port());
    }

    @Bean
    public RedissonClient redissonClient() {
        RedissonClient redisson;
        Config config = new Config();
        config.useSingleServer().setAddress("redis://%s:%d".formatted(properties.host(), properties.port()));
        redisson = Redisson.create(config);

        return redisson;
    }
}
