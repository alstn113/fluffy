package com.fluffy.submission.infra.persistence;


import static com.fluffy.auth.domain.QMember.member;
import static com.fluffy.submission.domain.QSubmission.submission;

import com.fluffy.submission.domain.SubmissionRepositoryCustom;
import com.fluffy.submission.domain.dto.ParticipantDto;
import com.fluffy.submission.domain.dto.QParticipantDto;
import com.fluffy.submission.domain.dto.QSubmissionSummaryDto;
import com.fluffy.submission.domain.dto.SubmissionSummaryDto;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SubmissionRepositoryImpl implements SubmissionRepositoryCustom {

    private static final ConstructorExpression<ParticipantDto> PARTICIPANT_PROJECTION = new QParticipantDto(
            member.id,
            member.name,
            member.email,
            member.avatarUrl
    );

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SubmissionSummaryDto> findSubmissionSummariesByExamId(Long examId) {
        return queryFactory
                .select(new QSubmissionSummaryDto(
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
