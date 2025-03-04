package com.team6.ticketbook.domain.member.controller

import com.team6.ticketbook.domain.member.dto.LoginRequest
import com.team6.ticketbook.domain.member.dto.LoginResponse
import com.team6.ticketbook.domain.member.dto.MemberResponse
import com.team6.ticketbook.domain.member.dto.RegisterRequest
import com.team6.ticketbook.domain.member.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    val authService: AuthService,
) {
    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest
    ): ResponseEntity<LoginResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(authService.login(request))
    }

    @PostMapping("/register")
    fun register(
        @RequestBody request: RegisterRequest
    ): ResponseEntity<MemberResponse> = ResponseEntity
        .status(HttpStatus.CREATED)
        .body(authService.register(request))

}