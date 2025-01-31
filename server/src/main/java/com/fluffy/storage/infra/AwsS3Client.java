package com.fluffy.storage.infra;

import com.fluffy.global.exception.BadRequestException;
import com.fluffy.global.exception.NotFoundException;
import com.fluffy.storage.application.StorageClient;
import io.awspring.cloud.s3.S3Exception;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class AwsS3Client implements StorageClient {

    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public String upload(MultipartFile file) {

        if (file.isEmpty()) {
            throw new BadRequestException("파일이 비어있습니다.");
        }

        String fileName = generateFileName(file.getOriginalFilename());

        try (InputStream is = file.getInputStream()) {
            S3Resource upload = s3Template.upload(bucketName, fileName, is);

            return upload.getURL().toString();
        } catch (IOException | S3Exception e) {
            throw new BadRequestException("파일 업로드에 실패했습니다.", e);
        }
    }

    @Override
    public void delete(String fileName) {
        try {
            s3Template.deleteObject(bucketName, fileName);
        } catch (S3Exception e) {
            throw new NotFoundException("파일을 찾을 수 없습니다.", e);
        }
    }

    private String generateFileName(String originalFileName) {
        if (originalFileName == null) {
            throw new NotFoundException("파일 이름을 찾을 수 없습니다.");
        }

        int extensionIndex = originalFileName.lastIndexOf(".");

        String extension = originalFileName.substring(extensionIndex);
        String fileName = originalFileName.substring(0, extensionIndex);

        String now = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS").format(System.currentTimeMillis());

        return "%s-%s%s".formatted(fileName, now, extension);
    }
}
