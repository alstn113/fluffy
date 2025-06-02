package com.fluffy.reaction.domain

data class Like(
    val target: LikeTarget,
    val targetId: Long,
)
