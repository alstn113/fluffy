package com.fluffy.comment.domain;

import com.fluffy.comment.domain.dto.ExamReplyCommentDto;
import com.fluffy.comment.domain.dto.ExamRootCommentDto;
import java.util.List;

public interface ExamCommentRepositoryCustom {

    List<ExamRootCommentDto> findRootComments(Long examId);

    List<ExamReplyCommentDto> findRootCommentWithReplies(Long commentId);
}
