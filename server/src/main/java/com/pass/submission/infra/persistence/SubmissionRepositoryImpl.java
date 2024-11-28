package com.pass.submission.infra.persistence;


import static com.pass.auth.domain.QMember.member;
import static com.pass.submission.domain.QSubmission.submission;

import com.pass.submission.domain.SubmissionRepositoryCustom;
import com.pass.submission.domain.dto.AnswerDto;
import com.pass.submission.domain.dto.ChoiceDto;
import com.pass.submission.domain.dto.ParticipantDto;
import com.pass.submission.domain.dto.QSubmissionDetailDto;
import com.pass.submission.domain.dto.QSubmissionSummaryDto;
import com.pass.submission.domain.dto.SubmissionDetailDto;
import com.pass.submission.domain.dto.SubmissionSummaryDto;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SubmissionRepositoryImpl implements SubmissionRepositoryCustom {

    private static final ConstructorExpression<ParticipantDto> PARTICIPANT_PROJECTION =
            Projections.constructor(ParticipantDto.class,
                    member.id,
                    member.name,
                    member.email,
                    member.avatarUrl
            );
//    private static final ConstructorExpression<SubmissionSummaryDto> ANSWER_PROJECTION =
//            Projections.constructor(AnswerDto.class,
//            );
//    private static final ConstructorExpression<SubmissionSummaryDto> CHOICE_PROJECTION =
//            Projections.constructor(ChoiceDto.class,
//            );

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SubmissionSummaryDto> findSummariesByExamId(Long examId) {
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

//    @Override
//    public Optional<SubmissionDetailDto> findDetail(Long submissionId) {
//        SubmissionDetailDto dto = queryFactory
//                .select(new QSubmissionDetailDto(
//                        submission.id,
//
//                        PARTICIPANT_PROJECTION
//
//                ))
//                .from(submission)
//                .leftJoin(member).on(submission.memberId.eq(member.id))
//                .leftJoin()
//                .where(submission.id.eq(submissionId))
//                .fetchOne();
//
//        return Optional.ofNullable(dto);
//    }
}
