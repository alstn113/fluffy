package com.fluffy.comment.domain.dto

import com.querydsl.core.annotations.QueryProjection

data class AuthorDto @QueryProjection constructor(
    val id: Long,
    val name: String,
    val avatarUrl: String
) {

    fun asDeleted() = AuthorDto(
        id = 0,
        name = "",
        avatarUrl = ""
    )
}
