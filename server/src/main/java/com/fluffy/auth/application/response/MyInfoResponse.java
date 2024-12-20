package com.fluffy.auth.application.response;

public record MyInfoResponse(
        Long id,
        String email,
        String name,
        String avatarUrl
) {
}
