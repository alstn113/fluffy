package com.fluffy.global.response

import org.springframework.data.domain.Page

data class PageResponse<T>(
    val pageInfo: PageInfo,
    val content: List<T>
) {
    companion object {
        fun <T> of(page: Page<T>): PageResponse<T> {
            return PageResponse(
                PageInfo(
                    page.number,
                    page.totalPages,
                    page.totalElements,
                    page.hasNext(),
                    page.hasPrevious()
                ),
                page.content
            )
        }
    }
}
