package com.pass.form.query.application;

import com.pass.global.exception.NotFoundException;

public class FormDataNotFoundException extends NotFoundException {

    private static final String MESSAGE = "존재하지 않는 폼입니다. formId: %s";

    public FormDataNotFoundException(String formId) {
        super(String.format(MESSAGE, formId));
    }

    public FormDataNotFoundException(String formId, Throwable cause) {
        super(String.format(MESSAGE, formId), cause);
    }
}
