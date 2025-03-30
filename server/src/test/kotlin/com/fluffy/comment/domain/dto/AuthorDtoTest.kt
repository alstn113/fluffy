package com.fluffy.comment.domain.dto

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class AuthorDtoTest : StringSpec({

    "삭제된 작성자의 정보를 가릴 수 있다." {
        // given
        val dto = AuthorDto(
            id = 1,
            name = "John",
            avatarUrl = "https://example.com/avatar.png"
        )

        // when
        val deletedDto = dto.asDeleted()

        // then
        deletedDto.id shouldBe 0
        deletedDto.name shouldBe ""
        deletedDto.avatarUrl shouldBe ""
    }
})