package com.team6.ticketbook.domain.lock.service

import com.team6.ticketbook.domain.lock.model.LockData
import com.team6.ticketbook.domain.lock.repository.LockRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LockService(
    private val lockRepository: LockRepository,
    @Value("\${lock.code}") private val lockCode: String,

    ) {
    @Transactional
    fun lock(code: String): LockData {
        return lockRepository.findByCode(lockCode) ?: run {
            LockData(
                code = code,
                lockNum = 0
            ).let { lockRepository.saveAndFlush(it) }
            lockRepository.findByCode(code)!!
        }
    }

}