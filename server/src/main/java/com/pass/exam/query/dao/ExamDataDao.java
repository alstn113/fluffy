package com.pass.exam.query.dao;

import com.pass.exam.query.application.exception.ExamDataNotFoundException;
import com.pass.exam.query.dto.ExamData;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface ExamDataDao extends Repository<ExamData, String>, ExamDataDaoCustom {

    List<ExamData> findAll();

    Optional<ExamData> findById(String examId);

    default ExamData getExamById(String examId) {
        return findById(examId)
                .orElseThrow(() -> new ExamDataNotFoundException(examId));
    }
}
