package com.fluffy.global.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class GlobalExceptionHandler {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }

    @ExceptionHandler(CoreException::class)
    fun handleCoreException(e: CoreException): ProblemDetail {
        log.warn("{}: {}", e::class.simpleName, e.message, e)

        return ProblemDetail.forStatusAndDetail(e.status, e.message)
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFoundException(e: NoResourceFoundException): ProblemDetail {
        log.warn("NoResourceFoundException: {}", e.message, e)

        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "요청하신 리소스를 찾을 수 없습니다.")
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(e: MethodArgumentTypeMismatchException): ProblemDetail {
        log.warn("MethodArgumentTypeMismatchException: {}", e.message, e)

        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "요청하신 데이터의 형식이 올바르지 않습니다.")
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ProblemDetail {
        log.warn("MethodArgumentNotValidException: {}", e.message, e)

        val errors = e.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "유효하지 않은 값입니다.") }
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.")
        problemDetail.setProperty("errors", errors)

        return problemDetail
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): ProblemDetail {
        log.warn("HttpRequestMethodNotSupportedException: {}", e.message, e)

        return ProblemDetail.forStatusAndDetail(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메소드입니다.")
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ProblemDetail {
        log.warn("HttpMessageNotReadableException: {}", e.message, e)

        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "요청 데이터를 읽을 수 없습니다.")
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ProblemDetail {
        log.error("Exception: {}", e.message, e)

        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내 예상치 못한 오류가 발생했습니다.")
    }
}