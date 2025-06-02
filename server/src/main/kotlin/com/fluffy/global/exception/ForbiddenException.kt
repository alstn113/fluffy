package com.fluffy.global.exception

import org.springframework.http.HttpStatus

class ForbiddenException(
    message: String,
    cause: Throwable? = null,
) : CoreException(HttpStatus.FORBIDDEN, message, cause)