package com.fluffy.submission.application;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import static com.fluffy.auth.domain.OAuth2Provider.GOOGLE;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.exam.domain.Question;
import com.fluffy.global.web.Accessor;
import com.fluffy.submission.application.request.QuestionResponseRequest;
import com.fluffy.submission.application.request.SubmissionRequest;
import com.fluffy.submission.domain.Submission;
import com.fluffy.submission.domain.SubmissionRepository;
import com.fluffy.support.AbstractIntegrationTest;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SubmissionServiceTest extends AbstractIntegrationTest {

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Test
    @DisplayName("한 번만 제출할 수 있는 시험에 중복 제출 요청이 들어오면 한 번만 제출된다.")
    void submit() throws InterruptedException {
        // given
        Member member1 = memberRepository.save(new Member("ex1@gmail.com", GOOGLE, "123", "ex1", "https://ex1.com"));
        Exam exam = Exam.create("시험 제목", member1.getId());
        exam.updateQuestions(List.of(Question.shortAnswer("단답형1", "지문", "답1")));
        exam.updateIsSingleAttempt(true);
        exam.publish();
        examRepository.save(exam);

        // when
        SubmissionRequest request = new SubmissionRequest(
                exam.getId(),
                List.of(new QuestionResponseRequest(List.of("답1"))),
                new Accessor(member1.getId())
        );
        try (ExecutorService executorService = newFixedThreadPool(2)) {
            CountDownLatch countDownLatch = new CountDownLatch(2);

            for (int i = 0; i < 2; i++) {
                executorService.execute(() -> {
                    try {
                        submissionService.submit(request);
                    } finally {
                        countDownLatch.countDown();
                    }
                });
            }

            countDownLatch.await();
        }

        // then
        List<Submission> submissions = submissionRepository.findAll();
        assertThat(submissions).hasSize(1);
    }
}
