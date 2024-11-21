package com.pass;

import com.pass.auth.domain.Member;
import com.pass.auth.domain.MemberRepository;
import com.pass.auth.domain.OAuth2Provider;
import com.pass.exam.command.domain.Exam;
import com.pass.exam.command.domain.ExamRepository;
import com.pass.exam.command.domain.ExamStatus;
import com.pass.exam.command.domain.QuestionGroup;
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

    private final MemberRepository memberRepository;
    private final ExamRepository examRepository;

    @Override
    public void run(ApplicationArguments args) {
        init();
    }

    private void init() {
//        Member member = new Member(
//                "example@gmail.com",
//                OAuth2Provider.GITHUB,
//                1234567890L,
//                "example",
//                "https://avatars.githubusercontent.com/u/1234567890"
//        );
//        memberRepository.save(member);

//        Exam exam = new Exam(
//                "국어 맞춤법 공부",
//                "국어 맞춤법을 제대로 알고 있는지 확인해보자!",
//                ExamStatus.PUBLISHED,
//                member.getId(),
//
//        );
    }
}
