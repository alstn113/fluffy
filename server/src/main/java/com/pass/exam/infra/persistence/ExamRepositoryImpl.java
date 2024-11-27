package com.pass.exam.infra.persistence;

import static com.pass.auth.domain.QMember.member;
import static com.pass.exam.domain.QExam.exam;
import static com.pass.exam.domain.QQuestion.question;

import com.pass.exam.domain.ExamRepositoryCustom;
import com.pass.exam.domain.ExamStatus;
import com.pass.exam.domain.dto.AuthorDto;
import com.pass.exam.domain.dto.ExamSummaryDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ExamRepositoryImpl implements ExamRepositoryCustom {

    private static final ConstructorExpression<AuthorDto> AUTHOR_PROJECTION = Projections.constructor(AuthorDto.class,
            member.id,
            member.name,
            member.avatarUrl
    );

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ExamSummaryDto> findAllExamSummaries() {
        return queryFactory
                .select(Projections.constructor(ExamSummaryDto.class,
                        exam.id,
                        exam.title,
                        exam.description,
                        exam.status,
                        AUTHOR_PROJECTION,
                        question.count(),
                        exam.createdAt,
                        exam.updatedAt
                ))
                .from(exam)
                .leftJoin(member).on(exam.memberId.eq(member.id))
                .leftJoin(exam.questionGroup.questions, question)
                .groupBy(exam.id)
                .orderBy(exam.updatedAt.desc())
                .fetch();
    }

    @Override
    public List<ExamSummaryDto> findMyExamSummaries(ExamStatus status, Long memberId) {
        return queryFactory
                .select(Projections.constructor(ExamSummaryDto.class,
                        exam.id,
                        exam.title,
                        exam.description,
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
                .groupBy(exam.id)
                .orderBy(exam.updatedAt.desc())
                .fetch();
    }
}
