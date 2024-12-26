package com.fluffy.exam.domain;

import com.fluffy.global.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Long>, ExamRepositoryCustom {

    default Exam findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 시험입니다. 시험 식별자: " + id));
    }
}
