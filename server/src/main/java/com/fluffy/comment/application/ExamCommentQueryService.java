package com.fluffy.comment.application;

import com.fluffy.comment.domain.ExamComment;
import com.fluffy.comment.domain.ExamCommentRepository;
import com.fluffy.comment.domain.dto.ExamReplyCommentDto;
import com.fluffy.comment.domain.dto.ExamRootCommentDto;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.global.exception.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExamCommentQueryService {

    private final ExamRepository examRepository;
    private final ExamCommentRepository examCommentRepository;

    @Transactional(readOnly = true)
    public List<ExamRootCommentDto> getRootComments(Long examId) {
        Exam exam = examRepository.findByIdOrThrow(examId);

        List<ExamRootCommentDto> rootComments = examCommentRepository.findRootComments(exam.getId());
        return maskIfDeleted(rootComments);
    }

    private List<ExamRootCommentDto> maskIfDeleted(List<ExamRootCommentDto> rootComments) {
        return rootComments.stream()
                .map(comment -> comment.isDeleted() ? comment.asDeleted() : comment)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ExamReplyCommentDto> getReplyComments(Long commentId) {
        ExamComment rootComment = examCommentRepository.findByIdOrThrow(commentId);
        List<ExamReplyCommentDto> replies = examCommentRepository.findRootCommentWithReplies(commentId);
        System.out.println("replies = " + replies);

        if (rootComment.isDeleted() && replies.isEmpty()) {
            throw new NotFoundException("존재하지 않는 댓글입니다. 댓글 식별자: %d".formatted(commentId));
        }

        return replies;
    }
}
