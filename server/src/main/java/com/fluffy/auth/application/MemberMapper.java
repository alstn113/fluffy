package com.fluffy.auth.application;

import com.fluffy.auth.application.dto.MyInfoResponse;
import com.fluffy.auth.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberMapper {

    public MyInfoResponse toMyInfoResponse(Member member) {
        return new MyInfoResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getAvatarUrl()
        );
    }
}
