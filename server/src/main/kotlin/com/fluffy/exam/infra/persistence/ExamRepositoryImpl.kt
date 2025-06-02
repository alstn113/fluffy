package com.fluffy.exam.infra.persistence

import com.fluffy.auth.domain.QMember.Companion.member
import com.fluffy.exam.domain.ExamRepositoryCustom
import com.fluffy.exam.domain.ExamStatus
import com.fluffy.exam.domain.QExam.Companion.exam
import com.fluffy.exam.domain.QQuestion.Companion.question
import com.fluffy.exam.domain.dto.AuthorDto
import com.fluffy.exam.domain.dto.ExamDetailSummaryDto
import com.fluffy.exam.domain.dto.ExamSummaryDto
import com.fluffy.exam.domain.dto.MyExamSummaryDto
import com.fluffy.exam.domain.dto.SubmittedExamSummaryDto
import com.fluffy.reaction.domain.LikeTarget
import com.fluffy.reaction.domain.QReaction.Companion.reaction
import com.fluffy.reaction.domain.ReactionStatus
import com.fluffy.reaction.domain.ReactionType
import com.fluffy.submission.domain.QSubmission.Companion.submission
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class ExamRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : ExamRepositoryCustom {

    companion object {
        private val AUTHOR_PROJECTION = Projections.constructor(
            AuthorDto::class.java,
            member.id,
            member.name,
            member.avatarUrl,
        )
    }

    override fun findPublishedExamSummaries(pageable: Pageable): Page<ExamSummaryDto> {
        val countQuery = queryFactory.select(exam.count())
            .from(exam)
            .where(exam.status.eq(ExamStatus.PUBLISHED))

        val examIds = getPagedExamIds(pageable)
        val count = countQuery.fetchOne() ?: 0

        if (examIds.isEmpty()) {
            return PageImpl(emptyList(), pageable, count)
        }

        val content = queryFactory
            .selectDistinct(
                Projections.constructor(
                    ExamSummaryDto::class.java,
                    exam.id,
                    exam._title.value,
                    exam._description.value,
                    exam.status,
                    AUTHOR_PROJECTION,
                    question.countDistinct(),
                    reaction.countDistinct(),
                    exam.createdAt,
                    exam.updatedAt,
                ),
            )
            .from(exam)
            .leftJoin(member).on(exam.memberId.eq(member.id))
            .leftJoin(exam._questions, question)
            .leftJoin(reaction).on(
                exam.id.eq(reaction.targetId)
                    .and(reaction.targetType.eq(LikeTarget.EXAM.name))
                    .and(reaction.type.eq(ReactionType.LIKE))
                    .and(reaction.status.eq(ReactionStatus.ACTIVE)),
            )
            .where(exam.id.`in`(examIds))
            .groupBy(
                exam.id,
                exam._title.value,
                exam._description.value,
                exam.status,
                member.id,
                member.name,
                member.avatarUrl,
                exam.createdAt,
                exam.updatedAt,
            )
            .orderBy(exam.updatedAt.desc())
            .fetch()

        return PageImpl(content, pageable, count)
    }

    private fun getPagedExamIds(pageable: Pageable): List<Long> {
        return queryFactory.select(exam.id)
            .from(exam)
            .where(exam.status.eq(ExamStatus.PUBLISHED))
            .orderBy(exam.updatedAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()
    }

    override fun findMyExamSummaries(pageable: Pageable, status: ExamStatus, memberId: Long): Page<MyExamSummaryDto> {
        val countQuery = queryFactory.select(exam.count())
            .from(exam)
            .where(exam.memberId.eq(memberId), exam.status.eq(status))

        val examIds = getMyExamIds(pageable, memberId, status)
        val count = countQuery.fetchOne() ?: 0

        if (examIds.isEmpty()) {
            return PageImpl(emptyList(), pageable, count)
        }

        val content = queryFactory
            .selectDistinct(
                Projections.constructor(
                    MyExamSummaryDto::class.java,
                    exam.id,
                    exam._title.value,
                    exam._description.value,
                    exam.status,
                    AUTHOR_PROJECTION,
                    question.countDistinct(),
                    exam.createdAt,
                    exam.updatedAt,
                ),
            )
            .from(exam)
            .leftJoin(member).on(exam.memberId.eq(member.id))
            .leftJoin(exam._questions, question)
            .where(exam.id.`in`(examIds))
            .groupBy(
                exam.id,
                exam._title.value,
                exam._description.value,
                exam.status,
                member.id,
                member.name,
                member.avatarUrl,
                exam.createdAt,
                exam.updatedAt,
            )
            .orderBy(exam.updatedAt.desc())
            .fetch()

        return PageImpl(content, pageable, count)
    }

    private fun getMyExamIds(pageable: Pageable, memberId: Long, status: ExamStatus): List<Long> {
        return queryFactory.select(exam.id)
            .from(exam)
            .where(exam.memberId.eq(memberId), exam.status.eq(status))
            .orderBy(exam.updatedAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()
    }

    override fun findSubmittedExamSummaries(pageable: Pageable, memberId: Long): Page<SubmittedExamSummaryDto> {
        val countQuery = queryFactory.select(exam.countDistinct())
            .from(exam)
            .join(submission).on(exam.id.eq(submission.examId))
            .where(submission.memberId.eq(memberId))

        val examIds = getSubmittedExamIds(pageable, memberId)
        val count = countQuery.fetchOne() ?: 0

        if (examIds.isEmpty()) {
            return PageImpl(emptyList(), pageable, count)
        }

        val content = queryFactory
            .select(
                Projections.constructor(
                    SubmittedExamSummaryDto::class.java,
                    exam.id,
                    exam._title.value,
                    exam._description.value,
                    AUTHOR_PROJECTION,
                    submission.countDistinct(),
                    submission.createdAt.max(),
                ),
            )
            .from(exam)
            .leftJoin(member).on(exam.memberId.eq(member.id))
            .join(submission).on(exam.id.eq(submission.examId))
            .where(exam.id.`in`(examIds).and(submission.memberId.eq(memberId)))
            .groupBy(
                exam.id,
                exam._title.value,
                exam._description.value,
                member.id,
                member.name,
                member.avatarUrl,
            )
            .orderBy(submission.createdAt.max().desc())
            .fetch()

        return PageImpl(content, pageable, count)
    }

    private fun getSubmittedExamIds(pageable: Pageable, memberId: Long): List<Long> {
        return queryFactory.select(exam.id)
            .from(exam)
            .join(submission).on(exam.id.eq(submission.examId))
            .where(submission.memberId.eq(memberId))
            .orderBy(submission.createdAt.max().desc())
            .groupBy(exam.id)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()
    }

    override fun findExamDetailSummary(examId: Long): ExamDetailSummaryDto? {
        return queryFactory
            .select(
                Projections.constructor(
                    ExamDetailSummaryDto::class.java,
                    exam.id,
                    exam._title.value,
                    exam._description.value,
                    exam.status,
                    AUTHOR_PROJECTION,
                    question.countDistinct(),
                    reaction.countDistinct(),
                    exam.createdAt,
                    exam.updatedAt,
                ),
            )
            .from(exam)
            .leftJoin(member).on(exam.memberId.eq(member.id))
            .leftJoin(exam._questions, question)
            .leftJoin(reaction).on(
                exam.id.eq(reaction.targetId)
                    .and(reaction.targetType.eq(LikeTarget.EXAM.name))
                    .and(reaction.type.eq(ReactionType.LIKE))
                    .and(reaction.status.eq(ReactionStatus.ACTIVE)),
            )
            .where(exam.id.eq(examId))
            .groupBy(
                exam.id,
                exam._title.value,
                exam._description.value,
                exam.status,
                member.id,
                member.name,
                member.avatarUrl,
                exam.createdAt,
                exam.updatedAt,
            )
            .fetchOne()
    }
}
