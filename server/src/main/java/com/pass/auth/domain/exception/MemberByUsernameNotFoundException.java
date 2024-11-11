package com.pass.auth.domain.exception;

import com.pass.global.exception.NotFoundException;

public class MemberByUsernameNotFoundException extends NotFoundException {

    private static final String MESSAGE = "존재하지 않는 사용자 이름입니다. 사용자 이름: %s";

    public MemberByUsernameNotFoundException(String username) {
        super(String.format(MESSAGE, username));
    }
}
