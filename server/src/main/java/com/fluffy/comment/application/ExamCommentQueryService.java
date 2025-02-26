package com.fluffy.comment.application;

import com.fluffy.comment.domain.ExamCommentRepository;
import com.fluffy.comment.domain.dto.ExamRootCommentDto;
import com.fluffy.comment.domain.dto.ExamRootCommentWithRepliesDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExamCommentQueryService {

    private final ExamCommentRepository examCommentRepository;

    @Transactional(readOnly = true)
    public List<ExamRootCommentDto> getRootComments(Long examId) {
        return examCommentRepository.findRootComments(examId);
    }

    @Transactional(readOnly = true)
    public ExamRootCommentWithRepliesDto getRootCommentWithReplies(Long examId, Long commentId) {
        return examCommentRepository.findRootCommentWithReplies(examId, commentId);
    }
}
