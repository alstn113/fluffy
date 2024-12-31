package com.fluffy.exam.infra.persistence;

import static com.fluffy.auth.domain.QMember.member;
import static com.fluffy.exam.domain.QExam.exam;
import static com.fluffy.exam.domain.QQuestion.question;
import static com.fluffy.submission.domain.QSubmission.submission;

import com.fluffy.exam.domain.ExamRepositoryCustom;
import com.fluffy.exam.domain.ExamStatus;
import com.fluffy.exam.domain.dto.AuthorDto;
import com.fluffy.exam.domain.dto.ExamSummaryDto;
import com.fluffy.exam.domain.dto.QAuthorDto;
import com.fluffy.exam.domain.dto.QExamSummaryDto;
import com.fluffy.exam.domain.dto.QSubmittedExamSummaryDto;
import com.fluffy.exam.domain.dto.SubmittedExamSummaryDto;
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
        List<ExamSummaryDto> content = queryFactory
                .selectDistinct(new QExamSummaryDto(
                        exam.id,
                        exam.title.value,
                        exam.description.value,
                        exam.status,
                        AUTHOR_PROJECTION,
                        question.count(),
                        exam.createdAt,
                        exam.updatedAt
                ))
                .from(exam)
                .leftJoin(member).on(exam.memberId.eq(member.id))
                .leftJoin(exam.questionGroup.questions, question)
                .where(exam.status.eq(ExamStatus.PUBLISHED))
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
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(exam.count())
                .from(exam)
                .where(exam.status.eq(ExamStatus.PUBLISHED));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<ExamSummaryDto> findMyExamSummaries(Pageable pageable, ExamStatus status, Long memberId) {
        List<ExamSummaryDto> content = queryFactory
                .selectDistinct(new QExamSummaryDto(
                        exam.id,
                        exam.title.value,
                        exam.description.value,
                        exam.status,
                        AUTHOR_PROJECTION,
                        question.count(),
                        exam.createdAt,
                        exam.updatedAt
                ))
                .from(exam)
                .leftJoin(member).on(exam.memberId.eq(member.id))
                .leftJoin(exam.questionGroup.questions, question)
                .where(exam.memberId.eq(memberId), exam.status.eq(status))
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
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(exam.count())
                .from(exam)
                .where(exam.memberId.eq(memberId), exam.status.eq(status));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<SubmittedExamSummaryDto> findSubmittedExamSummaries(Pageable pageable, Long memberId) {
        List<SubmittedExamSummaryDto> content = queryFactory
                .select(new QSubmittedExamSummaryDto(
                        exam.id,
                        exam.title.value,
                        exam.description.value,
                        submission.count(),
                        submission.createdAt.max()
                ))
                .from(exam)
                .join(submission).on(exam.id.eq(submission.examId))
                .where(submission.memberId.eq(memberId))
                .groupBy(
                        exam.id,
                        exam.title,
                        exam.description
                )
                .orderBy(submission.createdAt.max().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(submission.count())
                .from(exam)
                .join(submission).on(exam.id.eq(submission.examId))
                .where(submission.memberId.eq(memberId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
