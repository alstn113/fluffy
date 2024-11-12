package com.pass.auth.application.dto;

public record MyInfoResponse(
        Long id,
        String email,
        String name,
        String avatarUrl
) {
}
