package com.team6.ticketbook.domain.member.service

import com.team6.ticketbook.domain.member.dto.LoginRequest
import com.team6.ticketbook.domain.member.dto.LoginResponse
import com.team6.ticketbook.domain.member.dto.MemberResponse
import com.team6.ticketbook.domain.member.dto.RegisterRequest
import com.team6.ticketbook.domain.member.model.Member
import com.team6.ticketbook.domain.member.model.MemberRole
import com.team6.ticketbook.domain.member.repository.MemberRepository
import com.team6.ticketbook.infra.security.jwt.JwtHelper
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtHelper: JwtHelper
) {

    @Transactional
    fun register(request: RegisterRequest): MemberResponse {
        return Member(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            name = request.name,
            address = request.address,
            phoneNumber = request.phoneNumber
        ).let { memberRepository.save(it) }
            .let { MemberResponse.from(it) }
    }

    fun login(request: LoginRequest): LoginResponse {
        val member = memberRepository.findByEmail(request.email) ?: throw RuntimeException()
        if (!passwordEncoder.matches(request.password, member.password)) throw RuntimeException()
        return LoginResponse(
            accessToken = jwtHelper.generateAccessToken(
                subject = member.id.toString(),
                email = member.email,
                role = MemberRole.USER.name
            )
        )
    }
}