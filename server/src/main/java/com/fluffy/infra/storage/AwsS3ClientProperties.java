package com.fluffy.infra.storage;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.cloud.aws.s3")
public record AwsS3ClientProperties(
        @NotBlank String bucket,
        @NotBlank String domain
) {
}
