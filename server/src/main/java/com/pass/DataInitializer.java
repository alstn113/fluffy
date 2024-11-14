package com.pass;

import com.pass.auth.application.AuthService;
import com.pass.exam.command.application.ExamService;
import com.pass.submission.command.application.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Profile("local")
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final ExamService examService;
    private final AuthService authService;
    private final SubmissionService submissionService;

    @Override
    public void run(ApplicationArguments args) {
        init();
    }

    private void init() {
        // empty
    }
}
