package com.fluffy.exam.application;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamImage;
import com.fluffy.exam.domain.ExamImageRepository;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.global.exception.BadRequestException;
import com.fluffy.global.exception.ForbiddenException;
import com.fluffy.global.web.Accessor;
import com.fluffy.storage.application.StorageClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ExamImageService {

    private final StorageClient storageClient;
    private final ExamImageRepository examImageRepository;
    private final ExamRepository examRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public String uploadImage(Long examId, MultipartFile image, Accessor accessor) {
        validateExamAuthor(examId, accessor);

        UUID imageId = UUID.randomUUID();

        Long fileSize = image.getSize();
        String filePath = generateUploadPath(imageId, accessor.id(), image);

        ExamImage examImage = new ExamImage(imageId, accessor.id(), examId, filePath, fileSize);
        examImageRepository.save(examImage);

        try {
            return storageClient.upload(image, filePath);
        } catch (Exception e) {
            examImageRepository.delete(examImage);
            throw e;
        }
    }

    private void validateExamAuthor(Long examId, Accessor accessor) {
        Exam exam = examRepository.findByIdOrThrow(examId);
        Member member = memberRepository.findByIdOrThrow(accessor.id());

        if (exam.isNotWrittenBy(member.getId())) {
            throw new ForbiddenException("시험 작성자만 이미지를 업로드할 수 있습니다.");
        }
    }

    private String generateUploadPath(UUID imageId, Long memberId, MultipartFile image) {
        String originalFilename = image.getOriginalFilename();

        if (image.isEmpty() || originalFilename == null) {
            throw new BadRequestException("이미지 파일이 비어있습니다.");
        }

        int lastDotIndex = originalFilename.lastIndexOf(".");
        String extension = originalFilename.substring(lastDotIndex + 1);

        return "images/%d/exams/%s.%s".formatted(memberId, imageId, extension);
    }
}
