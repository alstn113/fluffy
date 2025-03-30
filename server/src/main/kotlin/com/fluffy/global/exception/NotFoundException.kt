package com.fluffy.global.exception

import org.springframework.http.HttpStatus

class NotFoundException(
    message: String,
    cause: Throwable? = null
) : CoreException(HttpStatus.NOT_FOUND, message, cause)