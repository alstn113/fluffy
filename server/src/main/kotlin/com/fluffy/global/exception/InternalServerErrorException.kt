package com.fluffy.global.exception

import org.springframework.http.HttpStatus

class InternalServerErrorException(
    message: String,
    cause: Throwable? = null,
) : CoreException(HttpStatus.INTERNAL_SERVER_ERROR, message, cause)