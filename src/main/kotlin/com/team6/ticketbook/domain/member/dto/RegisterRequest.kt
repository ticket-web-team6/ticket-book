package com.team6.ticketbook.domain.member.dto

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val address: String,
    val phoneNumber: String
)