package com.fluffy.global.redis.config;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RedissonConfig {

    private static final String REDIS_URL = "redis://%s:%d";
    private static final String REDIS_SSL_URL = "rediss://%s:%d";

    private final RedisProperties properties;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(String.format(createUrl(), properties.host(), properties.port()))
                .setSslEnableEndpointIdentification(properties.ssl().enabled());

        return Redisson.create(config);
    }

    private String createUrl() {
        if (Boolean.TRUE.equals(properties.ssl().enabled())) {
            return REDIS_SSL_URL;
        }
        return REDIS_URL;
    }
}
