package com.fluffy.exam.domain;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.exam.domain.dto.ExamSummaryDto;
import com.fluffy.exam.domain.dto.MyExamSummaryDto;
import com.fluffy.exam.domain.dto.SubmittedExamSummaryDto;
import com.fluffy.submission.domain.Answer;
import com.fluffy.submission.domain.Submission;
import com.fluffy.submission.domain.SubmissionRepository;
import com.fluffy.support.AbstractIntegrationTest;
import com.fluffy.support.data.MemberTestData;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class ExamRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Test
    @DisplayName("출제된 시험 요약 목록을 조회할 수 있다.")
    void findPublishedExamSummaries() {
        // given
        Member member1 = MemberTestData.defaultMember().build();
        memberRepository.save(member1);

        Member member2 = MemberTestData.defaultMember().build();
        memberRepository.save(member2);

        Exam publishedExam1 = Exam.create("publishedExam1", member1.getId());
        publishedExam1.updateQuestions(List.of(Question.shortAnswer("질문1", "지문", "답1")));
        publishedExam1.publish();
        examRepository.save(publishedExam1);

        Exam publishedExam2 = Exam.create("publishedExam2", member1.getId());
        publishedExam2.updateQuestions(List.of(
                Question.shortAnswer("질문3", "지문", "답3"),
                Question.shortAnswer("질문5", "지문", "답5")
        ));
        publishedExam2.publish();
        examRepository.save(publishedExam2);

        Exam draftExam1 = Exam.create("draftExam1", member1.getId());
        draftExam1.updateQuestions(List.of(Question.shortAnswer("질문2", "지문", "답2")));
        examRepository.save(draftExam1);

        Exam publishedExam3 = Exam.create("publishedExam3", member2.getId());
        publishedExam3.updateQuestions(List.of(
                Question.shortAnswer("질문4", "지문", "답4"),
                Question.shortAnswer("질문6", "지문", "답6"),
                Question.shortAnswer("질문7", "지문", "답7")
        ));
        publishedExam3.publish();
        examRepository.save(publishedExam3);

        // when
        PageRequest pageable = PageRequest.of(0, 2);
        Page<ExamSummaryDto> publishedExamSummaries = examRepository.findPublishedExamSummaries(pageable);

        // then
        assertAll(
                () -> assertThat(publishedExamSummaries.getTotalElements()).isEqualTo(3),
                () -> assertThat(publishedExamSummaries.getTotalPages()).isEqualTo(2),
                () -> assertThat(publishedExamSummaries.getNumber()).isZero(),
                () -> assertThat(publishedExamSummaries.getSize()).isEqualTo(2),
                () -> assertThat(publishedExamSummaries.getContent().stream().map(ExamSummaryDto::id))
                        .containsExactlyElementsOf(List.of(publishedExam3.getId(), publishedExam2.getId())),
                () -> assertThat(publishedExamSummaries.getContent().stream().map(ExamSummaryDto::questionCount))
                        .containsExactlyElementsOf(List.of(3L, 2L))
        );
    }

    @Test
    @DisplayName("내가 출제 중인 시험 요약 목록을 조회할 수 있다.")
    void findMyExamSummaries() {
        // given
        Member member1 = MemberTestData.defaultMember().build();
        memberRepository.save(member1);

        Member member2 = MemberTestData.defaultMember().build();
        memberRepository.save(member2);

        Exam publishedExam1 = Exam.create("publishedExam1", member1.getId());
        publishedExam1.updateQuestions(List.of(Question.shortAnswer("질문1", "지문", "답1")));
        publishedExam1.publish();
        examRepository.save(publishedExam1);

        Exam publishedExam2 = Exam.create("publishedExam2", member2.getId());
        publishedExam2.updateQuestions(List.of(
                Question.shortAnswer("질문3", "지문", "답3"),
                Question.shortAnswer("질문5", "지문", "답5")
        ));
        publishedExam2.publish();
        examRepository.save(publishedExam2);

        Exam draftExam1 = Exam.create("draftExam1", member1.getId());
        draftExam1.updateQuestions(List.of(Question.shortAnswer("질문2", "지문", "답2")));
        examRepository.save(draftExam1);

        Exam publishedExam3 = Exam.create("publishedExam3", member1.getId());
        publishedExam3.updateQuestions(List.of(
                Question.shortAnswer("질문4", "지문", "답4"),
                Question.shortAnswer("질문6", "지문", "답6"),
                Question.shortAnswer("질문7", "지문", "답7")
        ));
        publishedExam3.publish();
        examRepository.save(publishedExam3);

        // when
        PageRequest pageable = PageRequest.of(0, 2);
        Page<MyExamSummaryDto> myExamSummaries = examRepository.findMyExamSummaries(
                pageable,
                ExamStatus.PUBLISHED,
                member1.getId()
        );

        // then
        assertAll(
                () -> assertThat(myExamSummaries.getTotalElements()).isEqualTo(2),
                () -> assertThat(myExamSummaries.getTotalPages()).isEqualTo(1),
                () -> assertThat(myExamSummaries.getNumber()).isZero(),
                () -> assertThat(myExamSummaries.getSize()).isEqualTo(2),
                () -> assertThat(myExamSummaries.getContent().stream().map(MyExamSummaryDto::id))
                        .containsExactlyElementsOf(List.of(publishedExam3.getId(), publishedExam1.getId())),
                () -> assertThat(myExamSummaries.getContent().stream().map(MyExamSummaryDto::questionCount))
                        .containsExactlyElementsOf(List.of(3L, 1L))
        );
    }

    @Test
    @DisplayName("내가 제출한 시험 요약 목록을 조회할 수 있다.")
    void findSubmittedExamSummaries() {
        // given
        Member member1 = MemberTestData.defaultMember().build();
        memberRepository.save(member1);

        Member member2 = MemberTestData.defaultMember().build();
        memberRepository.save(member2);

        Exam publishedExam1 = Exam.create("publishedExam1", member1.getId());
        publishedExam1.updateQuestions(List.of(Question.shortAnswer("질문1", "지문", "답1")));
        publishedExam1.publish();
        examRepository.save(publishedExam1);

        Exam publishedExam2 = Exam.create("publishedExam2", member1.getId());
        publishedExam2.updateQuestions(List.of(Question.shortAnswer("질문1", "지문", "답1")));
        publishedExam2.publish();
        examRepository.save(publishedExam2);

        submissionRepository.save(new Submission(
                publishedExam1.getId(),
                member1.getId(),
                List.of(Answer.textAnswer(1L, "답1"))
        ));

        submissionRepository.save(new Submission(
                publishedExam2.getId(),
                member1.getId(),
                List.of(Answer.textAnswer(1L, "답2"))
        ));

        submissionRepository.save(new Submission(
                publishedExam1.getId(),
                member2.getId(),
                List.of(Answer.textAnswer(1L, "답3"))
        ));

        submissionRepository.save(new Submission(
                publishedExam1.getId(),
                member1.getId(),
                List.of(Answer.textAnswer(1L, "답1"))
        ));

        // when
        PageRequest pageable = PageRequest.of(0, 2);
        Page<SubmittedExamSummaryDto> submittedExamSummaries = examRepository.findSubmittedExamSummaries(
                pageable,
                member1.getId()
        );

        // then
        assertAll(
                () -> assertThat(submittedExamSummaries.getTotalElements()).isEqualTo(2),
                () -> assertThat(submittedExamSummaries.getTotalPages()).isEqualTo(1),
                () -> assertThat(submittedExamSummaries.getContent()
                        .stream()
                        .map(SubmittedExamSummaryDto::submissionCount)
                ).containsExactlyElementsOf(List.of(2L, 1L)),
                () -> assertThat(submittedExamSummaries.getContent()
                        .stream()
                        .map(SubmittedExamSummaryDto::title)
                ).containsExactlyElementsOf(List.of("publishedExam1", "publishedExam2"))
        );
    }
}
