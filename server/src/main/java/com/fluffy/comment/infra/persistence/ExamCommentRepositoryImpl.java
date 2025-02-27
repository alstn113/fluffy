package com.fluffy.comment.infra.persistence;

import static com.fluffy.auth.domain.QMember.member;
import static com.fluffy.comment.domain.QExamComment.examComment;

import com.fluffy.comment.domain.ExamCommentRepositoryCustom;
import com.fluffy.comment.domain.QExamComment;
import com.fluffy.comment.domain.dto.AuthorDto;
import com.fluffy.comment.domain.dto.ExamReplyCommentDto;
import com.fluffy.comment.domain.dto.ExamRootCommentDto;
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

    /**
     * 루트 댓글이 삭제되지 않았거나, 답글이 1개 이상 있는 루트 댓글들을 조회한다.
     */
    @Override
    public List<ExamRootCommentDto> findRootComments(Long examId) {
        QExamComment replyComment = new QExamComment("replyComment");

        return queryFactory.select(new QExamRootCommentDto(
                        examComment.id,
                        examComment.content,
                        AUTHOR_PROJECTION,
                        replyComment.id.count(),
                        examComment.deletedAt.isNotNull(),
                        examComment.createdAt,
                        examComment.updatedAt
                ))
                .from(examComment)
                .leftJoin(member).on(examComment.memberId.eq(member.id))
                .leftJoin(replyComment).on(examComment.id.eq(replyComment.parentCommentId)
                        .and(replyComment.deletedAt.isNull()))
                .where(examComment.examId.eq(examId).and(examComment.parentCommentId.isNull()))
                .orderBy(examComment.createdAt.asc())
                .groupBy(
                        examComment.id,
                        examComment.content,
                        AUTHOR_PROJECTION,
                        examComment.deletedAt,
                        examComment.createdAt,
                        examComment.updatedAt
                )
                .having(examComment.deletedAt.isNull().or(replyComment.id.count().gt(0)))
                .fetch();
    }

    /**
     * 루트 댓글과 삭제되지 않은 답글들을 조회한다.
     */
    @Override
    public List<ExamReplyCommentDto> findRootCommentWithReplies(Long commentId) {
        QExamComment replyComment = new QExamComment("replyComment");

        return queryFactory.select(new QExamReplyCommentDto(
                        replyComment.id,
                        replyComment.content,
                        AUTHOR_PROJECTION,
                        replyComment.createdAt,
                        replyComment.updatedAt
                ))
                .from(examComment)
                .leftJoin(member).on(examComment.memberId.eq(member.id))
                .join(replyComment).on(examComment.id.eq(replyComment.parentCommentId)
                        .and(replyComment.deletedAt.isNull()))
                .where(examComment.id.eq(commentId))
                .orderBy(replyComment.createdAt.asc())
                .fetch();
    }
}
