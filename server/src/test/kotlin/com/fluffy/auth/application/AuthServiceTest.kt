package com.fluffy.auth.application

import com.fluffy.auth.application.response.MyInfoResponse
import com.fluffy.auth.application.response.TokenResponse
import com.fluffy.auth.domain.MemberRepository
import com.fluffy.auth.domain.OAuth2Provider
import com.fluffy.auth.domain.findByIdOrThrow
import com.fluffy.auth.fixture.MemberFixture
import com.fluffy.global.exception.NotFoundException
import com.fluffy.oauth2.domain.OAuth2UserInfo
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class AuthServiceTest : BehaviorSpec({

    val tokenProvider = mockk<TokenProvider>()
    val memberRepository = mockk<MemberRepository>()
    val authService = AuthService(tokenProvider, memberRepository)

    Given("getMyInfo") {
        val memberId = 1L
        val member = MemberFixture.create(id = memberId)

        When("존재하는 사용자일 경우") {
            every { memberRepository.findByIdOrThrow(memberId) } returns member

            Then("사용자 정보를 반환한다.") {
                val actual = authService.getMyInfo(memberId)
                val expected = MyInfoResponse(
                    id = member.id,
                    email = member.email,
                    name = member.name,
                    avatarUrl = member.avatarUrl
                )

                actual shouldBe expected
            }
        }

        When("존재하지 않는 사용자일 경우") {
            every {
                memberRepository.findByIdOrThrow(memberId)
            } throws NotFoundException("존재하지 않는 사용자입니다. 사용자 식별자: $memberId")

            Then("예외를 반환한다.") {
                shouldThrow<NotFoundException> { authService.getMyInfo(memberId) }
                    .message shouldBe "존재하지 않는 사용자입니다. 사용자 식별자: $memberId"
            }
        }
    }

    Given("createToken") {
        val userInfo = mockk<OAuth2UserInfo>()
        val provider = OAuth2Provider.GITHUB
        val accessToken = "token"
        val member = MemberFixture.create(id = 1L)

        every { userInfo.socialId } returns "1234567890L"
        every { tokenProvider.createToken(any()) } returns accessToken
        every { userInfo.toMember(any()) } returns member

        When("기존에 해당하는 사용자가 있으면") {
            clearMocks(memberRepository)
            every { memberRepository.findBySocialIdAndProvider(any(), any()) } returns member

            val actual = authService.createToken(userInfo, provider)
            val expected = TokenResponse(accessToken)

            Then("사용자 정보를 이용하여 토큰을 생성한다.") {
                verify(exactly = 1) { memberRepository.findBySocialIdAndProvider(any(), any()) }
                verify(exactly = 0) { memberRepository.save(any()) }
                actual shouldBe expected
            }
        }

        When("해당 사용자가 없으면") {
            clearMocks(memberRepository)

            every { memberRepository.findBySocialIdAndProvider(any(), any()) } returns null
            every { memberRepository.save(any()) } returns member

            val actual = authService.createToken(userInfo, provider)
            val expected = TokenResponse(accessToken)

            Then("새로운 사용자를 생성하고 토큰을 생성한다.") {
                verify(exactly = 1) { memberRepository.findBySocialIdAndProvider(any(), any()) }
                verify(exactly = 1) { memberRepository.save(any()) }
                actual shouldBe expected
            }
        }
    }
})
