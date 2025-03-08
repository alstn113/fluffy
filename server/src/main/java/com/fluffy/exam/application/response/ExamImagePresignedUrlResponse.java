package com.fluffy.exam.application.response;

public record ExamImagePresignedUrlResponse(
        String presignedUrl,
        String imageUrl
) {
}
