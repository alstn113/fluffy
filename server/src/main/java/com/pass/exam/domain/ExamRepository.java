package com.pass.exam.domain;

import com.pass.global.exception.NotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface ExamRepository extends Repository<Exam, Long>, ExamRepositoryCustom {

    List<Exam> findAll();

    Exam save(Exam exam);

    Optional<Exam> findById(Long id);

    default Exam getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 시험입니다. 시험 식별자: " + id));
    }

}
