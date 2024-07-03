package com.team6.ticketbook.domain.member.dto

import com.team6.ticketbook.domain.member.model.Member

data class MemberResponse(
    val id: Long,
    val email: String,
    val name: String,
    val address: String,
    val phoneNumber: String,
) {
    companion object {
        fun from(member: Member): MemberResponse {
            return MemberResponse(
                id = member.id!!,
                email = member.email,
                name = member.name,
                address = member.address,
                phoneNumber = member.phoneNumber,
            )
        }
    }
}