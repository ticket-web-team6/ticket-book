package com.team6.ticketbook.domain.seat.repository

import com.team6.ticketbook.domain.seat.model.Seat
import org.springframework.data.jpa.repository.JpaRepository

interface SeatRepository : JpaRepository<Seat, Long> {
    fun findAllByPlaceId(placeId: Long): List<Seat>
}