package com.fluffy.reaction.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ReactionTest : StringSpec({

    "반응은 정상적으로 생성된다." {
        // given
        val reaction = Reaction.create(
            targetType = "EXAM",
            targetId = 1L,
            memberId = 1L,
            type = ReactionType.LIKE,
        )

        // when & then
        reaction.status shouldBe ReactionStatus.ACTIVE
    }

    "반응을 삭제하면 삭제 상태가 된다." {
        // given
        val reaction = Reaction.create(
            targetType = "EXAM",
            targetId = 1L,
            memberId = 1L,
            type = ReactionType.LIKE,
        )

        // when
        reaction.delete()

        // then
        reaction.status shouldBe ReactionStatus.DELETED
    }

    "반응을 활성화하면 활성 상태가 된다." {
        // given
        val reaction = Reaction.create(
            targetType = "EXAM",
            targetId = 1L,
            memberId = 1L,
            type = ReactionType.LIKE,
        )
        reaction.delete()

        // when
        reaction.activate()

        // then
        reaction.status shouldBe ReactionStatus.ACTIVE
    }
})