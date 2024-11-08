package com.pass.form.query.dao;

import com.pass.form.query.dto.FormData;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface FormDataDao extends Repository<FormData, String> {

    Optional<FormData> findById(String formId);
}
