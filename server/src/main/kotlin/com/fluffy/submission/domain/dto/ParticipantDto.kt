package com.fluffy.submission.domain.dto

import com.querydsl.core.annotations.QueryProjection

data class ParticipantDto @QueryProjection constructor(
    val id: Long,
    val name: String,
    val email: String?,
    val avatarUrl: String
)