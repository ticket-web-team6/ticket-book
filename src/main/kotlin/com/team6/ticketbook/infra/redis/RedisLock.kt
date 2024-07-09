package com.team6.ticketbook.infra.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RedisLock (

    private val redisTemplate: RedisTemplate<String, String>
) {

    private final val timeToLiveMilliseconds = 10000L

    fun lock(key: String): Boolean {
        println("Acquired Lock")
        return redisTemplate.opsForValue()
            .setIfAbsent(key, "lock", Duration.ofMillis(timeToLiveMilliseconds))
            ?: false
    }

    fun unlock(key: String): Boolean {
        return redisTemplate
            .delete(key)
            ?: false
    }

    fun <T> runExclusive(lockKey: String, func: () -> T): T {

        while (!lock(lockKey))
            Thread.sleep(100)

        return kotlin.runCatching { func.invoke() }
            .onSuccess { unlock(lockKey) }
            .onFailure { unlock(lockKey) }
            .getOrThrow()
    }

}
