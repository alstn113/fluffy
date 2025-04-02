package com.fluffy.infra.redis.config

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration


@Configuration
@EnableCaching
class RedisConfig(
    private val properties: RedisProperties
) {

    @Bean
    fun cacheManager(redisConnectionFactory: RedisConnectionFactory): RedisCacheManager {
        val cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(fromSerializer(GenericJackson2JsonRedisSerializer(createObjectMapper())))
            .disableCachingNullValues()
            .entryTtl { _, _ -> Duration.ofDays(3) } // 접근 시간 기준으로 3일 후 만료

        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(cacheConfig)
            .build()
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> {
        return RedisTemplate<String, Any>().apply {
            keySerializer = StringRedisSerializer()
            valueSerializer = GenericJackson2JsonRedisSerializer(createObjectMapper())
            connectionFactory = lettuceConnectionFactory()
        }
    }

    @Bean
    fun lettuceConnectionFactory(): LettuceConnectionFactory {
        val configuration = RedisStandaloneConfiguration().apply {
            hostName = properties.host
            port = properties.port
        }

        val clientConfig = LettuceClientConfiguration.builder().apply {
            if (properties.ssl.enabled) useSsl()
        }.build()

        return LettuceConnectionFactory(configuration, clientConfig)
    }

    private fun createObjectMapper(): ObjectMapper {
        val validator = BasicPolymorphicTypeValidator.builder()
            .allowIfBaseType(Any::class.java)
            .build()

        return ObjectMapper()
            .registerKotlinModule()
            .registerModules(JavaTimeModule()) // Java 8 날짜/시간 직렬화를 위한 모듈 등록
            .activateDefaultTyping(
                validator,
                DefaultTyping.EVERYTHING,
                JsonTypeInfo.As.PROPERTY
            )
    }
}
