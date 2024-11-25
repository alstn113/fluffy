package com.pass.exam.infra.persistence;

import com.pass.exam.domain.ExamRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExamRepositoryCustomImpl implements ExamRepositoryCustom {

    private final JPAQueryFactory queryFactory;
}
