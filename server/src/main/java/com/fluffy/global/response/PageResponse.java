package com.fluffy.global.response;

import java.util.List;
import org.springframework.data.domain.Page;

public record PageResponse<T>(
        PageInfo pageInfo,
        List<T> content
) {
    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
                new PageInfo(
                        page.getNumber(),
                        page.getTotalPages(),
                        page.getTotalElements(),
                        page.hasNext(),
                        page.hasPrevious()
                ),
                page.getContent()
        );
    }
}
