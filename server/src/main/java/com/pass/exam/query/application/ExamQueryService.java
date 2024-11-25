package com.pass.exam.query.application;

import com.pass.exam.query.dao.ExamDataDao;
import com.pass.exam.query.dto.ExamData;
import com.pass.exam.query.dto.ExamResponse;
import com.pass.exam.query.dto.ExamWithAnswersResponse;
import com.pass.global.web.Accessor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExamQueryService {

    private final ExamDataDao examDataDao;
    private final ExamDataMapper examDataMapper;

    @Transactional(readOnly = true)
    public ExamResponse getExam(Long examId) {
        ExamData examData = examDataDao.getExamById(examId);

        return examDataMapper.toResponse(examData);
    }

    @Transactional(readOnly = true)
    public List<ExamResponse> getExams() {
        List<ExamData> examDatas = examDataDao.findAll();

        return examDataMapper.toResponses(examDatas);
    }

    @Transactional(readOnly = true)
    public ExamWithAnswersResponse getExamWithAnswers(Long examId, Accessor accessor) {

        return new ExamWithAnswersResponse();
    }
}
