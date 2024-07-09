package com.team6.ticketbook.domain.lock.service

import com.team6.ticketbook.domain.lock.model.LockData
import com.team6.ticketbook.domain.lock.repository.LockRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

@Service
class LockService(
    private val lockRepository: LockRepository,
    private val redisTemplate: RedisTemplate<String, String>
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

    fun redisLock(key: String): Boolean {
        println("Acquired Lock")
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
}