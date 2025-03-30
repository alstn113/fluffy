package com.fluffy.global.exception

import org.springframework.http.HttpStatus

class UnauthorizedException(
    message: String,
    cause: Throwable? = null
) : CoreException(HttpStatus.UNAUTHORIZED, message, cause)