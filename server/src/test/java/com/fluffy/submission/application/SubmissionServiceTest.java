package com.fluffy.submission.application;

import static com.fluffy.auth.domain.OAuth2Provider.GOOGLE;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.assertj.core.api.Assertions.assertThat;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.exam.domain.Question;
import com.fluffy.global.web.Accessor;
import com.fluffy.submission.application.request.QuestionResponseAppRequest;
import com.fluffy.submission.application.request.SubmissionAppRequest;
import com.fluffy.submission.domain.Submission;
import com.fluffy.submission.domain.SubmissionRepository;
import com.fluffy.support.AbstractIntegrationTest;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import org.junit.jupiter.api.Disabled;
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
    @DisplayName("분산 서버에서 레디스를 사용한 분산 락을 이용해서 중복 제출을 방지한다.")
    @Disabled("레디스를 사용 중인 환경에서만 작동합니다.")
    void submit() throws InterruptedException {
        // given
        Member member1 = memberRepository.save(new Member("ex1@gmail.com", GOOGLE, "123", "ex1", "https://ex1.com"));
        Exam exam = Exam.create("시험 제목", member1.getId());
        exam.updateQuestions(List.of(Question.shortAnswer("단답형1", "답1")));
        exam.publish(null, null);
        examRepository.save(exam);

        // when
        SubmissionAppRequest request = new SubmissionAppRequest(
                exam.getId(),
                List.of(new QuestionResponseAppRequest(List.of("답1"))),
                new Accessor(member1.getId())
        );
        String lockName = "submit:%d:%d".formatted(exam.getId(), member1.getId());

        try (ExecutorService executorService = newFixedThreadPool(2)) {
            CountDownLatch countDownLatch = new CountDownLatch(2);

            for (int i = 0; i < 2; i++) {
                executorService.execute(() -> {
                    try {
                        submissionService.submit(request, lockName);
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
