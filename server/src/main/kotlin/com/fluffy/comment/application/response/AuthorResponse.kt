package com.fluffy.comment.application.response

import com.fluffy.auth.domain.Member

data class AuthorResponse(
    val id: Long,
    val name: String,
    val avatarUrl: String,
) {

    companion object {

        fun from(member: Member): AuthorResponse {
            return AuthorResponse(
                member.id,
                member.name,
                member.avatarUrl,
            )
        }
    }
}
