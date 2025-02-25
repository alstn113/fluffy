package com.fluffy.comment.application;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.comment.application.dto.CreateExamCommentRequest;
import com.fluffy.comment.application.dto.CreateExamCommentResponse;
import com.fluffy.comment.application.dto.DeleteExamCommentRequest;
import com.fluffy.comment.domain.ExamComment;
import com.fluffy.comment.domain.ExamCommentRepository;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.global.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExamCommentService {

    private final MemberRepository memberRepository;
    private final ExamRepository examRepository;
    private final ExamCommentRepository examCommentRepository;

    @Transactional
    public CreateExamCommentResponse createComment(CreateExamCommentRequest request) {
        Member member = memberRepository.findByIdOrThrow(request.memberId());
        Exam exam = examRepository.findByIdOrThrow(request.examId());

        if (exam.isNotPublished()) {
            throw new ForbiddenException("출제되지 않은 시험에는 댓글을 작성할 수 없습니다.");
        }

        boolean isRootComment = request.parentCommentId() == null;
        if (isRootComment) {
            ExamComment rootExamComment = createRootComment(request);
            return CreateExamCommentResponse.of(rootExamComment, member);
        }

        ExamComment replyExamComment = createReplyComment(request);
        return CreateExamCommentResponse.of(replyExamComment, member);
    }

    @Transactional
    public Long deleteComment(DeleteExamCommentRequest request) {
        ExamComment examComment = examCommentRepository.findByIdOrThrow(request.commentId());

        if (examComment.isNotWrittenBy(request.memberId())) {
            throw new ForbiddenException("댓글 작성자만 삭제할 수 있습니다.");
        }

        examComment.delete();
        return examComment.getId();
    }

    private ExamComment createRootComment(CreateExamCommentRequest request) {
        ExamComment rootExamComment = ExamComment.create(request.content(), request.examId(), request.memberId());

        return examCommentRepository.save(rootExamComment);
    }

    private ExamComment createReplyComment(CreateExamCommentRequest request) {
        ExamComment parentExamComment = examCommentRepository.findByIdOrThrow(request.parentCommentId());
        ExamComment replyExamComment = parentExamComment.reply(request.content(), request.memberId());

        return examCommentRepository.save(replyExamComment);
    }
}
