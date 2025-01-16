package com.fluffy.exam.infra.persistence;

import static com.fluffy.auth.domain.QMember.member;
import static com.fluffy.exam.domain.QExam.exam;
import static com.fluffy.exam.domain.QQuestion.question;
import static com.fluffy.reaction.domain.QReaction.reaction;
import static com.fluffy.submission.domain.QSubmission.submission;

import com.fluffy.exam.domain.ExamRepositoryCustom;
import com.fluffy.exam.domain.ExamStatus;
import com.fluffy.exam.domain.dto.AuthorDto;
import com.fluffy.exam.domain.dto.ExamSummaryDto;
import com.fluffy.exam.domain.dto.MyExamSummaryDto;
import com.fluffy.exam.domain.dto.QAuthorDto;
import com.fluffy.exam.domain.dto.QExamSummaryDto;
import com.fluffy.exam.domain.dto.QMyExamSummaryDto;
import com.fluffy.exam.domain.dto.QSubmittedExamSummaryDto;
import com.fluffy.exam.domain.dto.SubmittedExamSummaryDto;
import com.fluffy.reaction.domain.LikeTarget;
import com.fluffy.reaction.domain.ReactionStatus;
import com.fluffy.reaction.domain.ReactionType;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ExamRepositoryImpl implements ExamRepositoryCustom {

    private static final ConstructorExpression<AuthorDto> AUTHOR_PROJECTION = new QAuthorDto(
            member.id,
            member.name,
            member.avatarUrl
    );

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ExamSummaryDto> findPublishedExamSummaries(Pageable pageable) {
        JPAQuery<Long> countQuery = queryFactory.select(exam.count())
                .from(exam)
                .where(exam.status.eq(ExamStatus.PUBLISHED));

        List<Long> examIds = getPagedExamIds(pageable);

        List<ExamSummaryDto> content = queryFactory
                .selectDistinct(new QExamSummaryDto(
                        exam.id,
                        exam.title.value,
                        exam.description.value,
                        exam.status,
                        AUTHOR_PROJECTION,
                        question.countDistinct(),
                        reaction.countDistinct(),
                        exam.createdAt,
                        exam.updatedAt
                ))
                .from(exam)
                .leftJoin(member).on(exam.memberId.eq(member.id))
                .leftJoin(exam.questionGroup.questions, question)
                .leftJoin(reaction).on(
                        exam.id.eq(reaction.targetId)
                                .and(reaction.targetType.eq(LikeTarget.EXAM.name()))
                                .and(reaction.type.eq(ReactionType.LIKE))
                                .and(reaction.status.eq(ReactionStatus.ACTIVE))
                )
                .where(exam.id.in(examIds))
                .groupBy(
                        exam.id,
                        exam.title,
                        exam.description,
                        exam.status,
                        member.id,
                        member.name,
                        member.avatarUrl,
                        exam.createdAt,
                        exam.updatedAt
                )
                .orderBy(exam.updatedAt.desc())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private List<Long> getPagedExamIds(Pageable pageable) {
        List<Tuple> pagedExams = queryFactory.select(exam.id, exam.updatedAt)
                .from(exam)
                .where(exam.status.eq(ExamStatus.PUBLISHED))
                .orderBy(exam.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return pagedExams.stream()
                .map(tuple -> tuple.get(exam.id))
                .toList();
    }

    @Override
    public Page<MyExamSummaryDto> findMyExamSummaries(Pageable pageable, ExamStatus status, Long memberId) {
        JPAQuery<Long> countQuery = queryFactory.select(exam.count())
                .from(exam)
                .where(exam.memberId.eq(memberId), exam.status.eq(status));

        List<Long> examIds = getMyExamIds(pageable, memberId, status);

        List<MyExamSummaryDto> content = queryFactory
                .selectDistinct(new QMyExamSummaryDto(
                        exam.id,
                        exam.title.value,
                        exam.description.value,
                        exam.status,
                        AUTHOR_PROJECTION,
                        question.countDistinct(),
                        exam.createdAt,
                        exam.updatedAt
                ))
                .from(exam)
                .leftJoin(member).on(exam.memberId.eq(member.id))
                .leftJoin(exam.questionGroup.questions, question)
                .where(exam.id.in(examIds))
                .groupBy(
                        exam.id,
                        exam.title,
                        exam.description,
                        exam.status,
                        member.id,
                        member.name,
                        member.avatarUrl,
                        exam.createdAt,
                        exam.updatedAt
                )
                .orderBy(exam.updatedAt.desc())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private List<Long> getMyExamIds(Pageable pageable, Long memberId, ExamStatus status) {
        List<Tuple> pagedExams = queryFactory.select(exam.id, exam.updatedAt)
                .from(exam)
                .where(exam.memberId.eq(memberId), exam.status.eq(status))
                .orderBy(exam.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return pagedExams.stream()
                .map(tuple -> tuple.get(exam.id))
                .toList();
    }

    @Override
    public Page<SubmittedExamSummaryDto> findSubmittedExamSummaries(Pageable pageable, Long memberId) {
        JPAQuery<Long> countQuery = queryFactory.select(submission.count())
                .from(exam)
                .join(submission).on(exam.id.eq(submission.examId))
                .where(submission.memberId.eq(memberId));

        List<Long> examIds = getSubmittedExamIds(pageable, memberId);

        List<SubmittedExamSummaryDto> content = queryFactory
                .select(new QSubmittedExamSummaryDto(
                        exam.id,
                        exam.title.value,
                        exam.description.value,
                        AUTHOR_PROJECTION,
                        submission.countDistinct(),
                        reaction.countDistinct(),
                        submission.createdAt.max()
                ))
                .from(exam)
                .join(submission).on(exam.id.eq(submission.examId))
                .join(member).on(exam.memberId.eq(member.id))
                .join(reaction).on(
                        exam.id.eq(reaction.targetId)
                                .and(reaction.targetType.eq(LikeTarget.EXAM.name()))
                                .and(reaction.type.eq(ReactionType.LIKE))
                                .and(reaction.status.eq(ReactionStatus.ACTIVE))
                )
                .where(exam.id.in(examIds))
                .groupBy(
                        exam.id,
                        exam.title,
                        exam.description,
                        member.id,
                        member.name,
                        member.avatarUrl
                )
                .orderBy(submission.createdAt.max().desc())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private List<Long> getSubmittedExamIds(Pageable pageable, Long memberId) {
        List<Tuple> pagedExams = queryFactory.select(exam.id, submission.createdAt.max())
                .from(exam)
                .join(submission).on(exam.id.eq(submission.examId))
                .where(submission.memberId.eq(memberId))
                .orderBy(submission.createdAt.max().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return pagedExams.stream()
                .map(tuple -> tuple.get(exam.id))
                .toList();
    }
}
