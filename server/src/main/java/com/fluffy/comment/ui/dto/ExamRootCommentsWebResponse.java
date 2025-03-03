package com.fluffy.comment.ui.dto;

import com.fluffy.comment.domain.dto.ExamRootCommentDto;
import java.util.List;

public record ExamRootCommentsWebResponse(List<ExamRootCommentDto> comments) {
}
