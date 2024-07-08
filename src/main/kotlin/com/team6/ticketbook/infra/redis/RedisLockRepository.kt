package com.team6.ticketbook.infra.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RedisLockRepository (

    private val redisTemplate: RedisTemplate<String, Boolean>
) {

    private final val timeToLiveMilliseconds = 100L

    fun lock(key: String): Boolean =
        redisTemplate.opsForValue()
            .setIfAbsent(key, true, Duration.ofMillis(timeToLiveMilliseconds))
            ?: false

    fun unlock(key: String): Boolean =
        redisTemplate
            .delete(key)
            ?: false
}