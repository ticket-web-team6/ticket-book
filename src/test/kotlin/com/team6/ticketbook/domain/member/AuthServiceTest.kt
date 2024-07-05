package com.team6.ticketbook.domain.member

import com.team6.ticketbook.domain.member.exception.EmailAlreadyOccupiedException
import com.team6.ticketbook.domain.exception.InvalidCredentialException
import com.team6.ticketbook.domain.exception.ModelNotFoundException
import com.team6.ticketbook.domain.member.dto.LoginRequest
import com.team6.ticketbook.domain.member.dto.RegisterRequest
import com.team6.ticketbook.domain.member.model.Member
import com.team6.ticketbook.domain.member.repository.MemberRepository
import com.team6.ticketbook.domain.member.service.AuthService
import com.team6.ticketbook.infra.security.jwt.JwtHelper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.crypto.password.PasswordEncoder

class AuthServiceTest : BehaviorSpec({

    val issuer = "someIssuer"
    val accessTokenSecret = "someLongSecretSoThatThisShouldNotMakeAnyExceptionSoThatWeCouldKeepDoingTheTest"
    val accessTokenExpirationHour = 1

    val memberRepository = mockk<MemberRepository>(relaxed = true)
    val passwordEncoder = mockk<PasswordEncoder>(relaxed = true)
    val jwtHelper = JwtHelper(issuer, accessTokenSecret, accessTokenExpirationHour)
    val authService = AuthService(memberRepository, passwordEncoder, jwtHelper)

    val targetEmail = "test@test.com"
    val targetPassword = "somePassword"

    val registerRequest = RegisterRequest(
        email = targetEmail,
        password = "someEncryptedPasswsord",
        name = "Bro",
        address = "Space",
        phoneNumber = "404"
    )
    val loginRequest = LoginRequest(
        email = targetEmail,
        password = targetPassword
    )

    val comparisonMember = Member(
        id = 1,
        email = targetEmail,
        password = "someEncryptedPassword",
        name = "Bro",
        address = "Space",
        phoneNumber = "404"
    )

//    beforeTest {
//
//    }

    afterTest {
        clearAllMocks()
    }


    given("1-1. (가입하고자 하는) 이메일로 가입된 멤버가 이미 있을 때") {

        every { memberRepository.findByEmail(any()) } returns comparisonMember

        `when`("회원가입을 진행할 경우") {
            then("EmailAlreadyOccupiedException을 발생시켜야 한다") {

                shouldThrow<EmailAlreadyOccupiedException> {
                    authService.register(registerRequest)
                }
            }
        }
    }

    given("1-2. 아무 계정도 존재하지 않는 상황일 때") {

        every { memberRepository.findByEmail(any()) } returns null
        every { memberRepository.save(any()) } returns comparisonMember

        `when`("회원가입을 진행할 경우") {
            then("해당 정보로 만들어진 MemberResponse가 반환되어야 한다") {

                authService.register(registerRequest).let {
                    registerRequest.let { that ->
                        it.name shouldBeEqual that.name
                        it.email shouldBeEqual that.email
                        it.address shouldBeEqual that.address
                        it.phoneNumber shouldBeEqual that.phoneNumber
                    }
                }
            }
        }
    }

    given("2-1. 아무 계정도 존재하지 않는 상황일 때") {

        every { memberRepository.findByEmail(any()) } returns null

        `when`("어떤 이메일로(라도) 로그인을 진행할 경우") {
            then("ModelNotFoundException을 발생시켜야 한다") {

                shouldThrow<ModelNotFoundException> {
                    authService.login(loginRequest)
                }
            }
        }
    }

    given("2-2. (가입된 회원의) 이메일과 잘못된 비밀번호를 사용할 때") {

        every { memberRepository.findByEmail(any()) } returns comparisonMember
        every { passwordEncoder.matches(any(), any()) } returns false

        `when`("로그인을 진행할 경우") {
            then("InvalidCredentialsException을 발생시켜야 한다") {

                shouldThrow<InvalidCredentialException> {
                    authService.login(loginRequest)
                }
            }
        }
    }

    given("2-3. 존재하는 이메일과 올바른 비밀번호를 사용했을 때") {

        every { memberRepository.findByEmail(any()) } returns comparisonMember
        every { passwordEncoder.matches(any(), any()) } returns true

        `when`("로그인을 진행할 경우") {
            then("발급받은 토큰을 반환해야 한다") {

                val accessToken = authService.login(loginRequest).accessToken

                jwtHelper.validateToken(accessToken).isSuccess shouldBe true
            }
        }
    }
})