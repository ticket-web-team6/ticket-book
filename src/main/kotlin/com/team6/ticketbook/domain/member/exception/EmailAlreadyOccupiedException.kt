package com.team6.ticketbook.domain.member.exception

data class EmailAlreadyOccupiedException(
    override val message: String? = "Email already occupied by another member"
) : IllegalArgumentException()