package com.fluffy.comment.domain;

import com.fluffy.global.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamCommentRepository extends JpaRepository<ExamComment, Long> {

    default ExamComment findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 댓글입니다. 댓글 식별자: %d".formatted(id)));
    }
}

