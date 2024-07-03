package com.team6.ticketbook.domain.member.repository

import com.team6.ticketbook.domain.member.model.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {

}