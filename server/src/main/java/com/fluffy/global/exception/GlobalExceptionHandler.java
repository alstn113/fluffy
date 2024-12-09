package com.fluffy.global.exception;

import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail handleNoResourceFoundException(NoResourceFoundException e) {
        log.warn("[No Resource Found Exception]", e);

        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("[Method Argument Type Mismatch Exception]", e);

        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "잘못된 요청 타입입니다.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("[Method Argument Not Valid Exception]", e);

        Map<String, String> errors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("[Http Request Method Not Supported Exception]", e);

        return ProblemDetail.forStatusAndDetail(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메소드입니다.");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("[Http Message Not Readable Exception]", e);

        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "요청을 읽을 수 없습니다.");
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleBadRequestException(BadRequestException e) {
        log.warn("[Bad Request Exception]", e);

        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ProblemDetail handleUnauthorizedException(UnauthorizedException e) {
        log.warn("[Unauthorized Exception]", e);

        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ProblemDetail handleForbiddenException(ForbiddenException e) {
        log.warn("[Forbidden Exception]", e);

        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFoundException(NotFoundException e) {
        log.warn("[Not Found Exception]", e);

        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception e) {
        log.error("[Internal Server Error]", e);

        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");
    }
}