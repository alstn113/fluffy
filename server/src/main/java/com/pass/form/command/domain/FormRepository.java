package com.pass.form.command.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface FormRepository extends Repository<Form, String> {

    List<Form> findAll();

    Form save(Form form);

    Optional<Form> findById(String id);

    default Form getById(String id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Form not found id: %s".formatted(id)));
    }

}
