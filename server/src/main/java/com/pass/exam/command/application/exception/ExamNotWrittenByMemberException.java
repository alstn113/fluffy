package com.pass.exam.command.application.exception;

import com.pass.global.exception.ForbiddenException;

public class ExamNotWrittenByMemberException extends ForbiddenException {

    private static final String MESSAGE = "해당 사용자가 작성한 시험이 아닙니다. 사용자 식별자: %d, 시험 식별자: %s";

    public ExamNotWrittenByMemberException(Long memberId, String examId) {
        super(String.format(MESSAGE, memberId, examId));
    }
}
