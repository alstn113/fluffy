package com.fluffy.comment.infra.persistence;

import static com.fluffy.auth.domain.QMember.member;
import static com.fluffy.comment.domain.QExamComment.examComment;
import static com.querydsl.core.types.ExpressionUtils.count;

import com.fluffy.comment.domain.ExamCommentRepositoryCustom;
import com.fluffy.comment.domain.QExamComment;
import com.fluffy.comment.domain.dto.AuthorDto;
import com.fluffy.comment.domain.dto.ExamReplyCommentDto;
import com.fluffy.comment.domain.dto.ExamRootCommentDto;
import com.fluffy.comment.domain.dto.ExamRootCommentWithRepliesDto;
import com.fluffy.comment.domain.dto.QAuthorDto;
import com.fluffy.comment.domain.dto.QExamReplyCommentDto;
import com.fluffy.comment.domain.dto.QExamRootCommentDto;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ExamCommentRepositoryImpl implements ExamCommentRepositoryCustom {

    private static final ConstructorExpression<AuthorDto> AUTHOR_PROJECTION = new QAuthorDto(
            member.id,
            member.name,
            member.avatarUrl
    );

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ExamRootCommentDto> findRootComments(Long examId) {
        QExamComment parentComment = new QExamComment("parentComment");

        return queryFactory.select(new QExamRootCommentDto(
                        examComment.id,
                        examComment.content,
                        AUTHOR_PROJECTION,
                        count(parentComment.id),
                        examComment.deletedAt.isNotNull(),
                        examComment.createdAt,
                        examComment.updatedAt
                ))
                .from(examComment)
                .leftJoin(member).on(examComment.memberId.eq(member.id))
                .leftJoin(parentComment).on(examComment.parentCommentId.eq(parentComment.id))
                .where(examComment.examId.eq(examId).and(examComment.parentCommentId.isNull()))
                .orderBy(examComment.createdAt.desc())
                .groupBy(
                        examComment.id,
                        examComment.content,
                        AUTHOR_PROJECTION,
                        examComment.deletedAt,
                        examComment.createdAt,
                        examComment.updatedAt
                )
                .fetch();
    }

    @Override
    public ExamRootCommentWithRepliesDto findRootCommentWithReplies(Long examId, Long parentCommentId) {
        QExamComment replyComment = new QExamComment("replyComment");

        List<ExamReplyCommentDto> replies = queryFactory.select(new QExamReplyCommentDto(
                        replyComment.id,
                        replyComment.content,
                        AUTHOR_PROJECTION,
                        replyComment.deletedAt.isNotNull(),
                        replyComment.createdAt,
                        replyComment.updatedAt
                ))
                .from(examComment)
                .leftJoin(member).on(examComment.memberId.eq(member.id))
                .leftJoin(replyComment).on(examComment.id.eq(replyComment.parentCommentId))
                .where(examComment.examId.eq(examId).and(examComment.parentCommentId.eq(parentCommentId)))
                .orderBy(replyComment.createdAt.asc())
                .fetch();

        return new ExamRootCommentWithRepliesDto(parentCommentId, replies);
    }
}
