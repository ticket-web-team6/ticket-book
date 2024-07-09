package com.team6.ticketbook.domain.lock.repository

import com.team6.ticketbook.domain.lock.model.LockData
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import java.util.*

interface LockRepository : JpaRepository<LockData, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    fun findByCode(code: String): LockData?

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    override fun findById(id: Long): Optional<LockData>

}