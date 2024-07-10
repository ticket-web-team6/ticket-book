package com.team6.ticketbook.domain.lock.service

import com.team6.ticketbook.domain.lock.model.LockData
import com.team6.ticketbook.domain.lock.repository.LockRepository
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.util.concurrent.TimeUnit

@Service
class LockService(
    private val lockRepository: LockRepository,
    private val redisTemplate: RedisTemplate<String, String>,
    private val redissonClient: RedissonClient
) {

    private final val timeToLiveMilliseconds = 10000L

    @Transactional
    fun jpaLock(code: String): LockData {
        return lockRepository.findByCode(code) ?: run {
            LockData(
                code = code,
                lockNum = 0
            ).let { lockRepository.saveAndFlush(it) }
            lockRepository.findByCode(code)!!
        }
    }

    fun deleteLock(lock: LockData) {
        lockRepository.delete(lock)
    }

    fun redisLock(key: String): Boolean {
        return redisTemplate.opsForValue()
            .setIfAbsent(key, "lock", Duration.ofMillis(timeToLiveMilliseconds))
            ?: false
    }

    fun redisUnlock(key: String): Boolean {
        return redisTemplate
            .delete(key)
            ?: false
    }

    fun <T> runExclusive(lockKey: String, func: () -> T): T {

        while (!redisLock(lockKey))
            Thread.sleep(100)

        return kotlin.runCatching { func.invoke() }
            .onSuccess { redisUnlock(lockKey) }
            .onFailure { redisUnlock(lockKey) }
            .getOrThrow()
    }

    fun <T> runExclusiveWithRedissonLock(lockKey: String, func: () -> T): T {

        val lock: RLock = redissonClient.getFairLock(lockKey)

        return kotlin.runCatching {
            if (lock.tryLock(20, (timeToLiveMilliseconds / 1000), TimeUnit.SECONDS))
                func.invoke()
            else throw RuntimeException("Request timed out")
        }
            .onSuccess { lock.unlock() }
            .onFailure { lock.unlock() }
            .getOrThrow()
    }
}