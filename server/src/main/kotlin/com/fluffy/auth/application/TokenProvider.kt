package com.fluffy.auth.application

interface TokenProvider {

    fun createToken(memberId: String): String

    fun getMemberId(token: String): Long
}