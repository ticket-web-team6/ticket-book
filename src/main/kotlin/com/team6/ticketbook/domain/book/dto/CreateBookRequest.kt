package com.team6.ticketbook.domain.book.dto

import java.time.LocalDate

data class CreateBookRequest(
    val showId: Long,
    val memberId: Long,
    val seatId: Long,
    val date: LocalDate,
    val price: Int,
)
