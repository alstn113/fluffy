package com.fluffy.comment.domain;

import com.fluffy.comment.domain.dto.ExamRootCommentDto;
import com.fluffy.comment.domain.dto.ExamRootCommentWithRepliesDto;
import java.util.List;

public interface ExamCommentRepositoryCustom {

    List<ExamRootCommentDto> findRootComments(Long examId);

    ExamRootCommentWithRepliesDto findRootCommentWithReplies(Long examId, Long parentCommentId);
}
