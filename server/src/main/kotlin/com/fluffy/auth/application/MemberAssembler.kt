package com.fluffy.auth.application

import com.fluffy.auth.application.response.MyInfoResponse
import com.fluffy.auth.domain.Member

object MemberAssembler {

    fun toMyInfoResponse(member: Member): MyInfoResponse {
        return MyInfoResponse(
            id = member.id,
            email = member.email,
            name = member.name,
            avatarUrl = member.avatarUrl
        )
    }
}