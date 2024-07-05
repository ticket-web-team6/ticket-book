package com.team6.ticketbook.domain.member

import com.team6.ticketbook.domain.exception.ModelNotFoundException
import com.team6.ticketbook.domain.member.dto.UpdateMemberAddressRequest
import com.team6.ticketbook.domain.member.model.Member
import com.team6.ticketbook.domain.member.repository.MemberRepository
import com.team6.ticketbook.domain.member.service.MemberService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.equals.shouldNotBeEqual
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull
import java.util.*

class MemberServiceTest : BehaviorSpec ({

    val memberRepository = mockk<MemberRepository>()
    val memberService = MemberService(memberRepository)
    val memberId = 6L
    val newAddress = "Earth"

    val updateRequest = UpdateMemberAddressRequest(
        address = newAddress
    )

    val sampleMember = { Member(
        id = memberId,
        email = "test@test.com",
        password = "someEncryptedPassword",
        name = "Bro",
        address = "Space",
        phoneNumber = "404"
    ) }
    val comparisonMember = sampleMember()

//    beforeTest {
//
//    }

    afterTest {
        clearAllMocks()
    }

    given("1-1. 존재하지 않는 memberId를 사용할 때") {

        every { memberRepository.findByIdOrNull(any()) } returns null

        `when`("프로필 조회를 진행할 경우") {
            then("ModelNotFoundException을 발생시켜야 한다") {

                shouldThrow<ModelNotFoundException> {
                    memberService.getProfile(memberId)
                }
            }
        }
    }

    given("1-2. 존재하는 memberId를 사용할 때") {

        every { memberRepository.findByIdOrNull(any()) } returns sampleMember()

        `when`("프로필 조회를 진행할 경우") {
            then("해당 정보를 가지는 MemberResponse가 반환되어야 한다") {

                memberService.getProfile(memberId).let {
                    comparisonMember.let { that ->
                        it.id shouldBeEqual that.id!!
                        it.email shouldBeEqual that.email
                        it.name shouldBeEqual that.name
                        it.address shouldBeEqual that.address
                        it.phoneNumber shouldBeEqual that.phoneNumber
                    }
                }
            }
        }
    }


    given("2-1. 존재하지 않는 memberId를 사용할 때") {

        every { memberRepository.findByIdOrNull(any()) } returns null

        `when`("프로필 수정을 진행할 경우") {
            then("ModelNotFoundException을 발생시켜야 한다") {

                shouldThrow<ModelNotFoundException> {
                    memberService.updateMemberAddress(memberId, updateRequest)
                }
            }
        }
    }

    given("2-2. 존재하는 memberId를 사용할 때") {

        every { memberRepository.findByIdOrNull(any()) } returns sampleMember()

        `when`("프로필 조회를 진행할 경우") {
            then("해당 정보를 가지는 MemberResponse가 반환되어야 한다") {

                memberService.updateMemberAddress(memberId, updateRequest).let {
                    comparisonMember.let { that ->

                        it.id shouldBeEqual that.id!!
                        it.email shouldBeEqual that.email
                        it.name shouldBeEqual that.name
                        it.address shouldNotBeEqual that.address
                        it.address shouldBeEqual newAddress
                        it.phoneNumber shouldBeEqual that.phoneNumber
                    }
                }
            }
        }
    }



    given("3-1. 존재하지 않는 memberId를 사용할 때") {

        every { memberRepository.findByIdOrNull(any()) } returns null

        `when`("프로필 삭제를 진행할 경우") {
            then("ModelNotFoundException을 발생시켜야 한다") {

                shouldThrow<ModelNotFoundException> {
                    memberService.getProfile(memberId)
                }
            }
        }
    }

    given("3-2. 존재하는 memberId를 사용할 때") {

        every { memberRepository.findByIdOrNull(any()) } returns comparisonMember
        every { memberRepository.delete(comparisonMember) } answers {
            every { memberRepository.findByIdOrNull(any()) } returns null
        }

        `when`("프로필 삭제를 진행할 경우") {
            then("더 이상 조회가 불가능해야 한다") {

                memberService.deleteMemberById(memberId)

                shouldThrow<ModelNotFoundException> {
                    memberService.getProfile(memberId)
                }
            }
        }
    }
})