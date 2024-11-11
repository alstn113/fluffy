package com.pass.auth.domain.exception;

import com.pass.global.exception.NotFoundException;

public class MemberByIdNotFoundException extends NotFoundException {

    private static final String MESSAGE = "존재하지 않는 사용자입니다. 사용자 식별자: %d";

    public MemberByIdNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
