package com.fluffy.comment.infra.persistence

import com.fluffy.auth.domain.QMember.member
import com.fluffy.comment.domain.ExamCommentRepositoryCustom
import com.fluffy.comment.domain.QExamComment
import com.fluffy.comment.domain.QExamComment.examComment
import com.fluffy.comment.domain.dto.*
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class ExamCommentRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : ExamCommentRepositoryCustom {

    companion object {
        private val AUTHOR_PROJECTION = QAuthorDto(
            member.id,
            member.name,
            member.avatarUrl
        )
    }

    /**
     * 루트 댓글이 삭제되지 않았거나, 답글이 1개 이상 있는 루트 댓글들을 조회한다.
     */
    override fun findRootComments(examId: Long): List<ExamRootCommentDto> {
        val replyComment = QExamComment("replyComment")

        return queryFactory.select(
            QExamRootCommentDto(
                examComment.id,
                examComment.content,
                AUTHOR_PROJECTION,
                replyComment.id.count(),
                examComment.deletedAt.isNotNull,
                examComment.createdAt,
                examComment.updatedAt
            )
        )
            .from(examComment)
            .leftJoin(member).on(examComment.memberId.eq(member.id))
            .leftJoin(replyComment).on(
                examComment.id.eq(replyComment.parentCommentId)
                    .and(replyComment.deletedAt.isNull)
            )
            .where(examComment.examId.eq(examId).and(examComment.parentCommentId.isNull))
            .orderBy(examComment.createdAt.desc())
            .groupBy(
                examComment.id,
                examComment.content,
                AUTHOR_PROJECTION,
                examComment.deletedAt,
                examComment.createdAt,
                examComment.updatedAt
            )
            .having(examComment.deletedAt.isNull().or(replyComment.id.count().gt(0)))
            .fetch()
    }

    /**
     * 루트 댓글과 삭제되지 않은 답글들을 조회한다.
     */
    override fun findReplyComments(commentId: Long): List<ExamReplyCommentDto> {
        val replyComment = QExamComment("replyComment")

        return queryFactory.select(
            QExamReplyCommentDto(
                replyComment.id,
                replyComment.content,
                AUTHOR_PROJECTION,
                replyComment.createdAt,
                replyComment.updatedAt
            )
        )
            .from(examComment)
            .join(replyComment).on(
                examComment.id.eq(replyComment.parentCommentId)
                    .and(replyComment.deletedAt.isNull)
            )
            .leftJoin(member).on(replyComment.memberId.eq(member.id))
            .where(examComment.id.eq(commentId))
            .orderBy(replyComment.createdAt.desc())
            .fetch()
    }
}
