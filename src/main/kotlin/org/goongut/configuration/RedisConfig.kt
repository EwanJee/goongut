package web.remember.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
@EnableCaching
class RedisConfig {
    @Value("\${spring.data.redis.host}")
    lateinit var host: String

    @Value("\${spring.data.redis.port}")
    lateinit var port: String

    @Bean
    @Profile("[dev]")
    fun redisConnectionFactoryDev(): RedisConnectionFactory = LettuceConnectionFactory(host, port.toInt())

    @Bean
    @Profile("[production]")
    fun redisConnectionFactoryProd(): RedisConnectionFactory {
        val redisStandaloneConfig = RedisStandaloneConfiguration(host, 6379)
//        redisStandaloneConfig.password = RedisPassword.of(port)

        val clientConfig =
            LettuceClientConfiguration
                .builder()
                .useSsl() // 👉 TLS 활성화!
                .build()

        return LettuceConnectionFactory(redisStandaloneConfig, clientConfig)
    }

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, String> {
        val template = RedisTemplate<String, String>()
        template.connectionFactory = redisConnectionFactory
        template.keySerializer = StringRedisSerializer()
        template.hashKeySerializer = StringRedisSerializer()
        template.valueSerializer = StringRedisSerializer()
        template.hashValueSerializer = StringRedisSerializer()
        return template
    }

    @Bean
    fun cacheManager(redisConnectionFactory: RedisConnectionFactory): RedisCacheManager {
        val config =
            RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .disableCachingNullValues()

        return RedisCacheManager
            .builder(redisConnectionFactory)
            .cacheDefaults(config)
            .build()
    }
}
