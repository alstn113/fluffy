package com.pass.exam.application;

import com.pass.auth.domain.Member;
import com.pass.auth.domain.MemberRepository;
import com.pass.exam.application.dto.CreateExamAppRequest;
import com.pass.exam.application.dto.PublishExamAppRequest;
import com.pass.exam.application.dto.UpdateExamQuestionsAppRequest;
import com.pass.exam.application.dto.question.CreateExamResponse;
import com.pass.exam.domain.Exam;
import com.pass.exam.domain.ExamRepository;
import com.pass.exam.domain.Question;
import com.pass.global.exception.ForbiddenException;
import com.pass.global.web.Accessor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final ExamMapper examMapper;
    private final QuestionMapper questionMapper;
    private final MemberRepository memberRepository;

    @Transactional
    public CreateExamResponse create(CreateExamAppRequest request) {
        Accessor accessor = request.accessor();
        Member member = memberRepository.getById(accessor.id());

        Exam exam = Exam.create(request.title(), member.getId());
        Exam savedExam = examRepository.save(exam);

        return examMapper.toCreateResponse(savedExam);
    }

    @Transactional
    public void updateQuestions(UpdateExamQuestionsAppRequest request) {
        Exam exam = validateExamAuthor(request.examId(), request.accessor());

        List<Question> questions = questionMapper.toQuestions(request.questions());
        exam.updateQuestions(questions);
    }

    @Transactional
    public void publish(PublishExamAppRequest request) {
        Exam exam = validateExamAuthor(request.examId(), request.accessor());

        List<Question> questions = questionMapper.toQuestions(request.questions());
        exam.updateQuestions(questions);

        exam.publish(request.startAt(), request.endAt());
    }

    private Exam validateExamAuthor(Long examId, Accessor accessor) {
        Exam exam = examRepository.getById(examId);
        Member member = memberRepository.getById(accessor.id());

        if (exam.isNotWrittenBy(member.getId())) {
            throw new ForbiddenException(
                    "해당 사용자가 작성한 시험이 아닙니다. 사용자 식별자: %d, 시험 식별자: %d".formatted(member.getId(), exam.getId()));
        }

        return exam;
    }
}
