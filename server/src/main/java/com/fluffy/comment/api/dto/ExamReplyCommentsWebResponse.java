package com.fluffy.comment.api.dto;

import com.fluffy.comment.domain.dto.ExamReplyCommentDto;
import java.util.List;

public record ExamReplyCommentsWebResponse(List<ExamReplyCommentDto> replies) {
}
