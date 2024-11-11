package com.pass.exam.command.domain;

import com.pass.exam.command.domain.exception.ExamByIdNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface ExamRepository extends Repository<Exam, String> {

    List<Exam> findAll();

    Exam save(Exam exam);

    Optional<Exam> findById(String id);

    default Exam getById(String id) {
        return findById(id)
                .orElseThrow(() -> new ExamByIdNotFoundException(id));
    }

}
