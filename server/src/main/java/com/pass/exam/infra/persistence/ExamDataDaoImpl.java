package com.pass.exam.infra.persistence;

import com.pass.exam.query.dao.ExamDataDaoCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExamDataDaoImpl implements ExamDataDaoCustom {

    private final JPAQueryFactory queryFactory;
}
