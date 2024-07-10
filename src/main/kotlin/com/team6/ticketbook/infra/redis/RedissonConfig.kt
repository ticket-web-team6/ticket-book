package com.team6.ticketbook.infra.redis

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedissonConfig (

    private val redisProperties: RedisProperties
) {

    private val redissonHostPrefix: String = "redis://"

    @Bean
    fun redissonClient(): RedissonClient =
        Redisson.create(
            Config().apply {
                this.useSingleServer().address =
                    "${redissonHostPrefix}${redisProperties.host}:${redisProperties.port}"
            }
        )
}