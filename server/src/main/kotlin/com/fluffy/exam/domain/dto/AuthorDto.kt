package com.fluffy.exam.domain.dto

import com.querydsl.core.annotations.QueryProjection

data class AuthorDto @QueryProjection constructor(
    val id: Long,
    val name: String,
    val avatarUrl: String
)