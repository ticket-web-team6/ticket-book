package com.team6.ticketbook.domain.member.dto

data class LoginRequest(
    val email: String,
    val password: String
)