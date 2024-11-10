package com.pass.form.query.dao;

import com.pass.form.query.application.exception.FormDataNotFoundException;
import com.pass.form.query.dto.FormData;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface FormDataDao extends Repository<FormData, String>, FormDataDaoCustom {

    Optional<FormData> findById(String formId);

    default FormData getFormById(String formId) {
        return findById(formId)
                .orElseThrow(() -> new FormDataNotFoundException(formId));
    }
}
