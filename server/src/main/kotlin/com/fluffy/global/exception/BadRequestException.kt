package com.fluffy.global.exception

import org.springframework.http.HttpStatus

class BadRequestException(
    message: String,
    cause: Throwable? = null,
) : CoreException(HttpStatus.BAD_REQUEST, message, cause)

