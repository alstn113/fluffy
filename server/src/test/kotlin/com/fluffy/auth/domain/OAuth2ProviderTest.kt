package com.fluffy.auth.domain

import com.fluffy.global.exception.NotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class OAuth2ProviderTest : StringSpec({

    "존재하는 OAuth2 제공자는 정상적으로 반환된다." {
        // given
        val provider = "GITHUB"

        // when
        val oauth2Provider = OAuth2Provider.from(provider)

        // then
        oauth2Provider shouldBe OAuth2Provider.GITHUB
    }

    "대소문자 구분 없이 정상적으로 반환된다." {
        // given
        val provider = "github"

        // when
        val oauth2Provider = OAuth2Provider.from(provider)

        // then
        oauth2Provider shouldBe OAuth2Provider.GITHUB
    }

    "존재하지 않는 OAuth2 제공자는 예외를 던진다." {
        // given
        val invalidProvider = "INVALID_PROVIDER"

        // when & then
        shouldThrow<NotFoundException> { OAuth2Provider.from(invalidProvider) }
            .message shouldBe "존재하지 않는 OAuth2 제공자입니다. 제공자: $invalidProvider"
    }
})
