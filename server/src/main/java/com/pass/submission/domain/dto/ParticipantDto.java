package com.pass.submission.domain.dto;


public record ParticipantDto(
        Long id,
        String name,
        String email,
        String avatarUrl
) {
}
