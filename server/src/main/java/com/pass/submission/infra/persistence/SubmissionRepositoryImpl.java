package com.pass.submission.infra.persistence;


import static com.pass.auth.domain.QMember.member;
import static com.pass.submission.domain.QSubmission.submission;
import static com.querydsl.core.types.Projections.constructor;

import com.pass.submission.domain.SubmissionRepositoryCustom;
import com.pass.submission.domain.dto.ParticipantDto;
import com.pass.submission.domain.dto.SubmissionSummaryDto;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SubmissionRepositoryImpl implements SubmissionRepositoryCustom {

    private static final ConstructorExpression<ParticipantDto> PARTICIPANT_PROJECTION =
            constructor(ParticipantDto.class,
                    member.id,
                    member.name,
                    member.email,
                    member.avatarUrl
            );

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SubmissionSummaryDto> findSummariesByExamId(Long examId) {
        return queryFactory
                .select(constructor(SubmissionSummaryDto.class,
                        submission.id,
                        PARTICIPANT_PROJECTION,
                        submission.createdAt
                ))
                .from(submission)
                .leftJoin(member).on(submission.memberId.eq(member.id))
                .where(submission.examId.eq(examId))
                .orderBy(submission.createdAt.desc())
                .fetch();
    }
}
