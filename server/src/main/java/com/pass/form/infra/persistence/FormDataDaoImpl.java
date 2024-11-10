package com.pass.form.infra.persistence;

import com.pass.form.query.dao.FormDataDaoCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FormDataDaoImpl implements FormDataDaoCustom {

    private final JPAQueryFactory queryFactory;
}
