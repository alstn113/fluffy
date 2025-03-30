package com.fluffy.submission.infra.persistence

import com.fluffy.auth.domain.QMember.member
import com.fluffy.submission.domain.QSubmission.submission
import com.fluffy.submission.domain.SubmissionRepositoryCustom
import com.fluffy.submission.domain.dto.*
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class SubmissionRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : SubmissionRepositoryCustom {

    companion object {
        private val PARTICIPANT_PROJECTION = QParticipantDto(
            member.id,
            member.name,
            member.email,
            member.avatarUrl
        )
    }

    override fun findSubmissionSummariesByExamId(examId: Long): List<SubmissionSummaryDto> {
        return queryFactory
            .select(
                QSubmissionSummaryDto(
                    submission.id,
                    PARTICIPANT_PROJECTION,
                    submission.createdAt
                )
            )
            .from(submission)
            .leftJoin(member).on(submission.memberId.eq(member.id))
            .where(submission.examId.eq(examId))
            .orderBy(submission.createdAt.desc())
            .fetch()
    }

    override fun findMySubmissionSummaries(examId: Long, memberId: Long): List<MySubmissionSummaryDto> {
        return queryFactory
            .select(
                QMySubmissionSummaryDto(
                    submission.id,
                    submission.createdAt
                )
            )
            .from(submission)
            .where(submission.examId.eq(examId), submission.memberId.eq(memberId))
            .orderBy(submission.createdAt.desc())
            .fetch()
    }
}
