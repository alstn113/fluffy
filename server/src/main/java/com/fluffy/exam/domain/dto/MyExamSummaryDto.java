package com.fluffy.exam.domain.dto;

import com.fluffy.exam.domain.ExamStatus;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyExamSummaryDto {

    private Long id;
    private String title;
    private String description;
    private ExamStatus status;
    private AuthorDto author;
    private Long questionCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @QueryProjection
    public MyExamSummaryDto(
            Long id,
            String title,
            String description,
            ExamStatus status,
            AuthorDto author,
            Long questionCount,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.author = author;
        this.questionCount = questionCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}

