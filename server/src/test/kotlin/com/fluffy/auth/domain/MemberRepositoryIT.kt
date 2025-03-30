package com.fluffy.auth.domain

import com.fluffy.auth.fixture.MemberFixture
import com.fluffy.global.exception.NotFoundException
import com.fluffy.support.AbstractIntegrationTest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MemberRepositoryIT(
    private val memberRepository: MemberRepository,
) : AbstractIntegrationTest() {

    @Test
    fun `사용자 식별자로 사용자를 조회할 수 있다`() {
        // given
        val member = MemberFixture.create()
        val savedMember = memberRepository.save(member)

        // when
        val foundMember = memberRepository.findByIdOrThrow(savedMember.id)

        // then
        foundMember.id shouldBe savedMember.id
    }

    @Test
    fun `존재하지 않는 사용자 식별자로 사용자를 조회할 경우 예외를 발생시킨다`() {
        // given
        val nonExistentMemberId = -1L

        // when & then
        shouldThrow<NotFoundException> {
            memberRepository.findByIdOrThrow(nonExistentMemberId)
        }.message shouldBe "존재하지 않는 사용자입니다. 사용자 식별자: $nonExistentMemberId"
    }
}