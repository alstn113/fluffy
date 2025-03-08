package com.fluffy.global.storage.response;

public record PresignedUrlResponse(
        String presignedUrl,
        String fileUrl
) {
}
