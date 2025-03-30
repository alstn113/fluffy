package com.fluffy.infra.storage

import com.fluffy.global.storage.StorageClient
import com.fluffy.global.storage.response.PresignedUrlResponse
import io.awspring.cloud.s3.S3Template
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.net.URI
import java.time.Duration

@Component
class AwsS3Client(
    private val s3Template: S3Template,
    private val s3Presigner: S3Presigner,
    private val properties: AwsS3ClientProperties
) : StorageClient {

    companion object {
        private val PRESIGNED_URL_EXPIRATION: Duration = Duration.ofMinutes(5)
    }

    override fun getPresignedUrl(filePath: String): PresignedUrlResponse {
        val presignRequest = buildPresignedRequest(filePath)

        val presignedUrl = s3Presigner.presignPutObject(presignRequest).url().toString()
        val fileUrl = createFileUrl(filePath)

        return PresignedUrlResponse(presignedUrl, fileUrl)
    }

    private fun createFileUrl(filePath: String): String {
        val domain = URI.create(properties.domain)

        return domain.resolve(filePath).toString()
    }

    private fun buildPresignedRequest(filePath: String): PutObjectPresignRequest {
        val requestBuilder = PutObjectRequest.builder()
            .bucket(properties.bucket)
            .key(filePath)

        return PutObjectPresignRequest.builder()
            .signatureDuration(PRESIGNED_URL_EXPIRATION)
            .putObjectRequest(requestBuilder.build())
            .build()
    }

    override fun delete(filePath: String) {
        s3Template.deleteObject(properties.bucket, filePath)
    }
}