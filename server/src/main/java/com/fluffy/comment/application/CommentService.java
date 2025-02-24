package com.fluffy.comment.application;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.comment.application.dto.CreateCommentRequest;
import com.fluffy.comment.application.dto.CreateCommentResponse;
import com.fluffy.comment.application.dto.DeleteCommentRequest;
import com.fluffy.comment.domain.Comment;
import com.fluffy.comment.domain.CommentRepository;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.global.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final ExamRepository examRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CreateCommentResponse createComment(CreateCommentRequest request) {
        Member member = memberRepository.findByIdOrThrow(request.memberId());
        Exam exam = examRepository.findByIdOrThrow(request.examId());

        if (exam.isNotPublished()) {
            throw new ForbiddenException("출제되지 않은 시험에는 댓글을 작성할 수 없습니다.");
        }

        boolean isRootComment = request.parentCommentId() == null;
        if (isRootComment) {
            Comment rootComment = createRootComment(request);
            return CreateCommentResponse.of(rootComment, member);
        }

        Comment replyComment = createReplyComment(request);
        return CreateCommentResponse.of(replyComment, member);
    }

    @Transactional
    public Long deleteComment(DeleteCommentRequest request) {
        Comment comment = commentRepository.findByIdOrThrow(request.commentId());

        if (comment.isNotWrittenBy(request.memberId())) {
            throw new ForbiddenException("댓글 작성자만 삭제할 수 있습니다.");
        }

        comment.delete();
        return comment.getId();
    }

    private Comment createRootComment(CreateCommentRequest request) {
        Comment rootComment = Comment.create(request.content(), request.examId(), request.memberId());

        return commentRepository.save(rootComment);
    }

    private Comment createReplyComment(CreateCommentRequest request) {
        Comment parentComment = commentRepository.findByIdOrThrow(request.parentCommentId());
        Comment replyComment = parentComment.reply(request.content(), request.memberId());

        return commentRepository.save(replyComment);
    }
}
