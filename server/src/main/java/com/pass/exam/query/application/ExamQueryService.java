package com.pass.exam.query.application;

import com.pass.exam.query.dao.ExamDataDao;
import com.pass.exam.query.dto.ExamData;
import com.pass.exam.query.dto.ExamDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExamQueryService {

    private final ExamDataDao examDataDao;
    private final ExamDataMapper examDataMapper;

    @Transactional(readOnly = true)
    public ExamDataResponse getExam(String examId) {
        ExamData examData = examDataDao.getExamById(examId);

        return examDataMapper.toResponse(examData);
    }
}
