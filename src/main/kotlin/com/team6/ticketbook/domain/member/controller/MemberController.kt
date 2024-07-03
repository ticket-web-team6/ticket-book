package com.team6.ticketbook.domain.member.controller

import com.team6.ticketbook.domain.member.dto.MemberResponse
import com.team6.ticketbook.domain.member.dto.UpdateMemberAddressRequest
import com.team6.ticketbook.domain.member.service.MemberService
import com.team6.ticketbook.infra.security.MemberPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/members")
class MemberController(
    private val memberService: MemberService
) {

    @GetMapping("/profile")
    fun getProfile(
        @AuthenticationPrincipal member: MemberPrincipal
    ): ResponseEntity<MemberResponse> = ResponseEntity
        .status(HttpStatus.OK)
        .body(memberService.getProfile(member.id))


    @PatchMapping("/edit-address")
    fun updateMemberAddress(
        @RequestBody request: UpdateMemberAddressRequest,
        @AuthenticationPrincipal member: MemberPrincipal
    ): ResponseEntity<MemberResponse> = ResponseEntity
        .status(HttpStatus.OK)
        .body(memberService.updateMemberAddress(member.id, request))


    @DeleteMapping
    fun deleteMemberById(
        @AuthenticationPrincipal member: MemberPrincipal
    ): ResponseEntity<Unit> = ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body(memberService.deleteMemberById(member.id))

}