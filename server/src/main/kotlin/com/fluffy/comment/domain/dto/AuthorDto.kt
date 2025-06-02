package com.fluffy.comment.domain.dto

data class AuthorDto(
    val id: Long,
    val name: String,
    val avatarUrl: String,
) {

    fun asDeleted() = AuthorDto(
        id = 0,
        name = "",
        avatarUrl = "",
    )
}
