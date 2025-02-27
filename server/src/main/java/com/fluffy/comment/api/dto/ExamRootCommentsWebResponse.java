package com.fluffy.comment.api.dto;

import com.fluffy.comment.domain.dto.ExamRootCommentDto;
import java.util.List;

public record ExamRootCommentsWebResponse(List<ExamRootCommentDto> comments) {
}
