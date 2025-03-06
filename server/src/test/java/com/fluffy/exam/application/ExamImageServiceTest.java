package com.fluffy.exam.application;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.exam.application.request.ExamImagePresignedUrlRequest;
import com.fluffy.exam.application.response.ExamImagePresignedUrlResponse;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamImageRepository;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.global.exception.ForbiddenException;
import com.fluffy.global.storage.response.PresignedUrlResponse;
import com.fluffy.global.web.Accessor;
import com.fluffy.support.AbstractIntegrationTest;
import com.fluffy.support.data.MemberTestData;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class ExamImageServiceTest extends AbstractIntegrationTest {

    @Autowired
    private ExamImageService examImageService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamImageRepository examImageRepository;

    @Test
    @DisplayName("시험 이미지에 대한 Presigend URL을 생성할 수 있다.")
    void createPresignedUrl() {
        Member member = createMember();
        Exam exam = createExam(member);

        String presignedUrl = "https://cdn.fluffy.run/exams/1/QWER-1234.png?X-Amz-BLABLA=1234";
        String fileUrl = "https://cdn.fluffy.run/exams/1/QWER-1234.png";
        when(storageClient.getPresignedUrl(any()))
                .thenReturn(new PresignedUrlResponse(presignedUrl, fileUrl));

        ExamImagePresignedUrlRequest request = new ExamImagePresignedUrlRequest("BLA-BLA.png", 1024L);
        Accessor accessor = new Accessor(member.getId());

        ExamImagePresignedUrlResponse response = examImageService.createPresignedUrl(exam.getId(), request, accessor);

        assertAll(
                () -> assertThat(response.presignedUrl()).isEqualTo(presignedUrl),
                () -> assertThat(response.imageUrl()).isEqualTo(fileUrl),
                () -> assertThat(examImageRepository.findAll()).hasSize(1)
        );
    }

    @Test
    @DisplayName("시험에 대한 작성자만 해당 Presigned URL을 생성할 수 있다.")
    void createPresignedUrlWithDifferentAuthor() {
        Member member = createMember();
        Member anotherMember = createAnotherMember();
        Exam exam = createExam(anotherMember);

        ExamImagePresignedUrlRequest request = new ExamImagePresignedUrlRequest("BLA-BLA.png", 1024L);
        Accessor accessor = new Accessor(member.getId());

        Long examId = exam.getId();
        assertThatThrownBy(() -> examImageService.createPresignedUrl(examId, request, accessor))
                .isInstanceOf(ForbiddenException.class);
    }

    private Member createMember() {
        Member member = MemberTestData.defaultMember().build();
        return memberRepository.save(member);
    }

    private Member createAnotherMember() {
        Member member = MemberTestData.defaultMember().withId(2L).build();
        return memberRepository.save(member);
    }

    private Exam createExam(Member member) {
        Exam exam = Exam.create("시험 제목", member.getId());
        return examRepository.save(exam);
    }

    private MultipartFile createMockMultipartFile() {
        return new MockMultipartFile(
                "image",
                "image.png",
                "image/png",
                "image".getBytes()
        );
    }
}
