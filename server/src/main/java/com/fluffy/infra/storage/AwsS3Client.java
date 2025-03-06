package com.fluffy.infra.storage;

import com.fluffy.global.storage.StorageClient;
import com.fluffy.global.storage.response.PresignedUrlResponse;
import io.awspring.cloud.s3.S3Template;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
@RequiredArgsConstructor
public class AwsS3Client implements StorageClient {

    private static final Duration PRESIGNED_URL_EXPIRATION = Duration.ofMinutes(5);

    private final S3Template s3Template;
    private final S3Presigner s3Presigner;
    private final AwsS3ClientProperties properties;

    @Override
    public PresignedUrlResponse getPresignedUrl(String filePath) {
        PutObjectPresignRequest presignRequest = buildPresignedRequest(filePath);

        String presignedUrl = s3Presigner.presignPutObject(presignRequest).url().toString();
        String fileUrl = properties.domain() + filePath;

        return new PresignedUrlResponse(presignedUrl, fileUrl);
    }

    @Override
    public void delete(String filePath) {
        s3Template.deleteObject(properties.bucket(), filePath);
    }

    private PutObjectPresignRequest buildPresignedRequest(String filePath) {
        PutObjectRequest.Builder requestBuilder = PutObjectRequest.builder()
                .bucket(properties.bucket())
                .key(filePath);

        return PutObjectPresignRequest.builder()
                .signatureDuration(PRESIGNED_URL_EXPIRATION)
                .putObjectRequest(requestBuilder.build())
                .build();
    }
}
