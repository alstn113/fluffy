package com.fluffy.auth.application

import com.fluffy.auth.application.response.MyInfoResponse
import com.fluffy.auth.application.response.TokenResponse
import com.fluffy.auth.domain.Member
import com.fluffy.auth.domain.MemberRepository
import com.fluffy.auth.domain.OAuth2Provider
import com.fluffy.auth.domain.findByIdOrThrow
import com.fluffy.oauth2.domain.OAuth2UserInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val tokenProvider: TokenProvider,
    private val memberRepository: MemberRepository,
) {

    fun getMemberIdByToken(token: String): Long {
        return tokenProvider.getMemberId(token)
    }

    @Transactional(readOnly = true)
    fun getMyInfo(memberId: Long): MyInfoResponse {
        val member = memberRepository.findByIdOrThrow(memberId)

        return MemberAssembler.toMyInfoResponse(member)
    }

    @Transactional
    fun createToken(userInfo: OAuth2UserInfo, provider: OAuth2Provider): TokenResponse {
        val member = findOrCreateMember(userInfo, provider)
        val accessToken = tokenProvider.createToken(member.id.toString())

        return TokenResponse(accessToken)
    }

    private fun findOrCreateMember(userInfo: OAuth2UserInfo, provider: OAuth2Provider): Member {
        return memberRepository.findBySocialIdAndProvider(userInfo.socialId, provider)
            ?: memberRepository.save(userInfo.toMember(provider))
    }
}