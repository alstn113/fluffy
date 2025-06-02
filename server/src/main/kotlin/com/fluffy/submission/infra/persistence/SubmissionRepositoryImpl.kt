package com.fluffy.submission.infra.persistence

import com.fluffy.auth.domain.QMember.Companion.member
import com.fluffy.submission.domain.QSubmission.Companion.submission
import com.fluffy.submission.domain.SubmissionRepositoryCustom
import com.fluffy.submission.domain.dto.MySubmissionSummaryDto
import com.fluffy.submission.domain.dto.ParticipantDto
import com.fluffy.submission.domain.dto.SubmissionSummaryDto
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class SubmissionRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : SubmissionRepositoryCustom {

    companion object {
        private val PARTICIPANT_PROJECTION = Projections.constructor(
            ParticipantDto::class.java,
            member.id,
            member.name,
            member.email,
            member.avatarUrl,
        )
    }

    override fun findSubmissionSummariesByExamId(examId: Long): List<SubmissionSummaryDto> {
        return queryFactory
            .select(
                Projections.constructor(
                    SubmissionSummaryDto::class.java,
                    submission.id,
                    PARTICIPANT_PROJECTION,
                    submission.createdAt,
                ),
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
                Projections.constructor(
                    MySubmissionSummaryDto::class.java,
                    submission.id,
                    submission.createdAt,
                ),
            )
            .from(submission)
            .where(submission.examId.eq(examId), submission.memberId.eq(memberId))
            .orderBy(submission.createdAt.desc())
            .fetch()
    }
}
