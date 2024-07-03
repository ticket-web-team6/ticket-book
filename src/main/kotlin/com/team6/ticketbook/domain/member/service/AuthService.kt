package com.team6.ticketbook.domain.member.service

import com.team6.ticketbook.domain.member.dto.MemberResponse
import com.team6.ticketbook.domain.member.dto.RegisterRequest
import com.team6.ticketbook.domain.member.model.Member
import com.team6.ticketbook.domain.member.repository.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val memberRepository: MemberRepository
) {

    @Transactional
    fun register(request: RegisterRequest): MemberResponse {
        return Member(
            email = request.email,
            password = request.password,
            name = request.name,
            address = request.address,
            phoneNumber = request.phoneNumber
        ).let { memberRepository.save(it) }
            .let { MemberResponse.from(it) }
    }
}