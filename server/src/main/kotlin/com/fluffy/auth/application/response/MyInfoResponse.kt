package com.fluffy.auth.application.response

data class MyInfoResponse(
    val id: Long,
    val email: String?,
    val name: String,
    val avatarUrl: String,
)
