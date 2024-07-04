package com.team6.ticketbook.domain.seat.dto

data class SeatResponse(
    val id: Long,
    val seatCode: String,
    val grade: String,
    val isTaken: Boolean,
)