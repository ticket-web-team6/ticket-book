package com.team6.ticketbook.domain.member.controller

import com.team6.ticketbook.domain.member.dto.MemberResponse
import com.team6.ticketbook.domain.member.dto.UpdateMemberAddressRequest
import com.team6.ticketbook.domain.member.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/members")
class MemberController(
    private val memberService: MemberService
) {

    @GetMapping("/{memberId}")
    fun getMemberById(
        @PathVariable memberId: Long
    ): ResponseEntity<MemberResponse> = ResponseEntity
        .status(HttpStatus.OK)
        .body(memberService.getMemberById(memberId))


    @PatchMapping("/{memberId}/edit-address")
    fun updateMemberAddress(
        @PathVariable memberId: Long,
        @RequestBody request: UpdateMemberAddressRequest
    ): ResponseEntity<MemberResponse> = ResponseEntity
        .status(HttpStatus.OK)
        .body(memberService.updateMemberAddress(memberId, request))


    @DeleteMapping("/{memberId}")
    fun deleteMemberById(
        @PathVariable memberId: Long
    ): ResponseEntity<Unit> = ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body(memberService.deleteMemberById(memberId))

}