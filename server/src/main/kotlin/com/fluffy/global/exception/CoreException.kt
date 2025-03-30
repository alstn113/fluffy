package com.fluffy.global.exception

import org.springframework.http.HttpStatus

abstract class CoreException(
    val status: HttpStatus,
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)