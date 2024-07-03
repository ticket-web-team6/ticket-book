package com.team6.ticketbook.domain.member.service

import com.team6.ticketbook.domain.member.dto.MemberResponse
import com.team6.ticketbook.domain.member.dto.UpdateMemberAddressRequest
import com.team6.ticketbook.domain.member.repository.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service


@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
    fun getMemberById(memberId: Long): MemberResponse {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw RuntimeException()
        return MemberResponse.from(member)
    }

    @Transactional
    fun updateMemberAddress(memberId: Long, request: UpdateMemberAddressRequest): MemberResponse {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw RuntimeException()
        return member.apply {
            this.address = request.address
        }.let { MemberResponse.from(member) }
    }

    @Transactional
    fun deleteMemberById(memberId: Long) {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw RuntimeException()
        memberRepository.delete(member)
    }
}