package com.fluffy.exam.application;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.exam.application.request.ExamImagePresignedUrlRequest;
import com.fluffy.exam.application.response.ExamImagePresignedUrlResponse;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamImage;
import com.fluffy.exam.domain.ExamImageRepository;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.global.exception.ForbiddenException;
import com.fluffy.global.storage.StorageClient;
import com.fluffy.global.storage.response.PresignedUrlResponse;
import com.fluffy.global.web.Accessor;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExamImageService {

    private final StorageClient storageClient;
    private final ExamImageRepository examImageRepository;
    private final ExamRepository examRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ExamImagePresignedUrlResponse createPresignedUrl(
            Long examId,
            ExamImagePresignedUrlRequest request,
            Accessor accessor
    ) {
        validateExamAuthor(examId, accessor);

        UUID imageId = UUID.randomUUID();
        String imagePath = createImagePath(imageId, request.imageName());

        ExamImage examImage = new ExamImage(imageId, accessor.id(), examId, imagePath, request.fileSize());
        examImageRepository.save(examImage);

        PresignedUrlResponse response = storageClient.getPresignedUrl(imagePath);
        return new ExamImagePresignedUrlResponse(response.presignedUrl(), response.fileUrl());
    }

    private void validateExamAuthor(Long examId, Accessor accessor) {
        Exam exam = examRepository.findByIdOrThrow(examId);
        Member member = memberRepository.findByIdOrThrow(accessor.id());

        if (exam.isNotWrittenBy(member.getId())) {
            throw new ForbiddenException("시험 작성자만 이미지를 업로드할 수 있습니다.");
        }
    }

    private String createImagePath(UUID imageId, String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        return "/exams/%s.%s".formatted(imageId, extension);
    }
}