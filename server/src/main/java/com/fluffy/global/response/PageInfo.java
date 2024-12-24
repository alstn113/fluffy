package com.fluffy.global.response;

public record PageInfo(
        int currentPage,
        int totalPages,
        long totalElements,
        boolean hasNext,
        boolean hasPrevious
) {
}
