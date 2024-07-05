package com.team6.ticketbook.domain.book.exception

data class SeatAlreadyTakenException(
    override val message: String? = "Seat already taken"
) : RuntimeException()