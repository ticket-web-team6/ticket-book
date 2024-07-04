package com.team6.ticketbook.domain.book.exception

data class InvalidDateException(
    override val message: String? = "Input date is not valid"
) : RuntimeException()