package com.fluffy.global.web.cookie

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "auth.cookie")
data class CookieProperties(
    val accessTokenKey: String,
    val httpOnly: Boolean,
    val secure: Boolean,
    val domain: String,
    val path: String,
    val maxAge: Long
)
