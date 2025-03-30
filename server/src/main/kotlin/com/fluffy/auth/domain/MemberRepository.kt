package com.fluffy.auth.domain

import com.fluffy.global.exception.NotFoundException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

interface MemberRepository : JpaRepository<Member, Long> {

    fun findBySocialIdAndProvider(socialId: String, provider: OAuth2Provider): Member?
}

fun MemberRepository.findByIdOrThrow(id: Long): Member {
    return findByIdOrNull(id)
        ?: throw NotFoundException("존재하지 않는 사용자입니다. 사용자 식별자: $id")
}

