package com.fluffy.infra.storage;

import io.awspring.cloud.s3.S3Template;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
@RequiredArgsConstructor
public class AwsS3Client implements StorageClient {

    private static final Duration PRESIGNED_URL_EXPIRATION = Duration.ofMinutes(5);

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    private final S3Template s3Template;
    private final S3Presigner s3Presigner;

    @Override
    public String getPresignedUrl(String fileName) {
        PutObjectPresignRequest presignRequest = buildPresignedRequest(fileName);

        return s3Presigner.presignPutObject(presignRequest)
                .url()
                .toString();
    }

    @Override
    public void delete(String fileName) {
        s3Template.deleteObject(bucket, fileName);
    }

    private PutObjectPresignRequest buildPresignedRequest(String fileName) {
        PutObjectRequest.Builder requestBuilder = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName);

        return PutObjectPresignRequest.builder()
                .signatureDuration(PRESIGNED_URL_EXPIRATION)
                .putObjectRequest(requestBuilder.build())
                .build();
    }
}
