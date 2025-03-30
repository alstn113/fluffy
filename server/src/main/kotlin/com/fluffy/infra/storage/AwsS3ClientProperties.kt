package com.fluffy.infra.storage

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.cloud.aws.s3")
data class AwsS3ClientProperties(
    @field:NotBlank val bucket: String,
    @field:NotBlank val domain: String,
)