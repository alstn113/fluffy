package com.fluffy.auth.fixture

import com.fluffy.auth.domain.Member
import com.fluffy.auth.domain.OAuth2Provider

object MemberFixture {

    fun create(id: Long = 0L): Member {
        return Member(
            id = id,
            email = "example@gmail.com",
            provider = OAuth2Provider.GITHUB,
            socialId = "1234567890L",
            name = "example",
            avatarUrl = "https://example.com/avatar.jpg"
        )
    }
}