package com.fluffy.submission.domain.dto

data class ParticipantDto(
    val id: Long,
    val name: String,
    val email: String?,
    val avatarUrl: String,
)