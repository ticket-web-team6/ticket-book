package com.team6.ticketbook.infra.redis

import com.team6.ticketbook.domain.show.dto.ShowResponse
import com.team6.ticketbook.domain.show.model.Show
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Page
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
@EnableRedisRepositories
class LettuceConfig (

    private val redisProperties: RedisProperties
) {

    @Bean
    fun redisConnectionFactory(): LettuceConnectionFactory =
        LettuceConnectionFactory(redisProperties.host, redisProperties.port)

    @Bean
    fun redisTemplate(redisConnectionFactory: LettuceConnectionFactory): RedisTemplate<String, Any> =
        RedisTemplate<String, Any>().apply {
            this.connectionFactory = redisConnectionFactory

            this.keySerializer = StringRedisSerializer()
            this.valueSerializer = StringRedisSerializer()

            this.hashKeySerializer = StringRedisSerializer()
            this.hashValueSerializer = StringRedisSerializer()

            this.setEnableTransactionSupport(true)
        }

    @Bean
    fun showRedisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Page<ShowResponse>> {
        val template = RedisTemplate<String, Page<ShowResponse>>()
        template.connectionFactory = redisConnectionFactory
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = GenericJackson2JsonRedisSerializer()
        return template
    }
}