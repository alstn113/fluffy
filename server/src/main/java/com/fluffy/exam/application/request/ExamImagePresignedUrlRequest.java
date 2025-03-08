package com.fluffy.exam.application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExamImagePresignedUrlRequest(
        @NotNull @NotBlank String imageName,
        long fileSize
) {
}
