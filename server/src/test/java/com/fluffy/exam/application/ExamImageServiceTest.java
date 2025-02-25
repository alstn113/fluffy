package com.fluffy.exam.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamImageRepository;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.global.exception.BadRequestException;
import com.fluffy.global.exception.ForbiddenException;
import com.fluffy.global.web.Accessor;
import com.fluffy.support.AbstractIntegrationTest;
import com.fluffy.support.data.MemberTestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    @DisplayName("시험 제작 시 시험 문제에 대한 이미지를 업로드할 수 있다.")
    void uploadImage() {
        // given
        Member member = createMember();
        Exam exam = createExam(member);
        Accessor accessor = new Accessor(member.getId());
        MultipartFile image = createMockMultipartFile();

        // when
        String returnImageUrl = "https://fluffy-bucket.amazonaws.com/images/4/exams/uuid.png";
        when(storageClient.upload(any(), any())).thenReturn(returnImageUrl);
        String imageUrl = examImageService.uploadImage(exam.getId(), image, accessor);

        // then
        assertAll(
                () -> assertThat(imageUrl).isEqualTo(returnImageUrl),
                () -> assertThat(examImageRepository.findAll()).hasSize(1)
        );
    }

    @Test
    @DisplayName("시험 제작자가 아닌 경우 해당 시험에 대해 이미지를 업로드할 수 없다.")
    void uploadImage_notAuthor() {
        // given
        Member member = createMember();
        Exam exam = createExam(member);

        Member anotherMember = createAnotherMember();

        Long examId = exam.getId();
        MultipartFile image = createMockMultipartFile();
        Accessor accessor = new Accessor(anotherMember.getId());

        // when & then
        assertThatThrownBy(() -> examImageService.uploadImage(examId, image, accessor))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("시험 작성자만 이미지를 업로드할 수 있습니다.");
    }

    @Test
    @DisplayName("이미지 파일이 비어있는 경우 이미지를 업로드할 수 없다.")
    void uploadImage_emptyImage() {
        // given
        Member member = createMember();
        Exam exam = createExam(member);

        Long examId = exam.getId();
        MultipartFile image = new MockMultipartFile("image", "image.png", "image/png", new byte[0]);
        Accessor accessor = new Accessor(member.getId());

        // when & then
        assertThatThrownBy(() -> examImageService.uploadImage(examId, image, accessor))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("이미지 파일이 비어있습니다.");
    }

    @Test
    @DisplayName("외부 이미지 저장소에 이미지 업로드 실패 시 저장된 이미지 정보를 삭제한다.")
    void uploadImage_fail() {
        // given
        Member member = createMember();
        Exam exam = createExam(member);
        Long examId = exam.getId();
        MultipartFile image = createMockMultipartFile();
        Accessor accessor = new Accessor(member.getId());

        // when
        when(storageClient.upload(any(), any())).thenThrow(new RuntimeException());

        // then
        assertAll(
                () -> assertThatThrownBy(() -> examImageService.uploadImage(examId, image, accessor))
                        .isInstanceOf(RuntimeException.class),
                () -> assertThat(examImageRepository.findAll()).isEmpty()
        );
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
