package com.pass.form.command.application.exception;

import com.pass.global.exception.ForbiddenException;

public class FormNotWrittenByMemberException extends ForbiddenException {

    private static final String MESSAGE = "해당 사용자가 작성한 폼이 아닙니다. 사용자 식별자: %d, 양식 식별자: %s";

    public FormNotWrittenByMemberException(Long memberId, String formId) {
        super(String.format(MESSAGE, memberId, formId));
    }
}
