package com.pass.exam.command.application;

import com.pass.auth.domain.Member;
import com.pass.auth.domain.MemberRepository;
import com.pass.exam.command.application.dto.CreateExamAppRequest;
import com.pass.exam.command.application.dto.CreateExamResponse;
import com.pass.exam.command.application.dto.PublishExamAppRequest;
import com.pass.exam.command.application.exception.ExamNotWrittenByMemberException;
import com.pass.exam.command.domain.Exam;
import com.pass.exam.command.domain.ExamRepository;
import com.pass.exam.command.domain.QuestionGroup;
import com.pass.global.web.Accessor;
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

        Exam exam = Exam.initial(request.title(), member.getId());
        Exam savedExam = examRepository.save(exam);

        return examMapper.toResponse(savedExam);
    }

    @Transactional
    public void publish(PublishExamAppRequest request) {
        Exam exam = examRepository.getById(request.examId());
        Accessor accessor = request.accessor();
        Member member = memberRepository.getById(accessor.id());

        if (exam.isNotWrittenBy(member.getId())) {
            throw new ExamNotWrittenByMemberException(member.getId(), exam.getId());
        }

        QuestionGroup questionGroup = questionMapper.toQuestionGroup(request, exam);
        exam.addQuestions(questionGroup, exam);
    }
}
