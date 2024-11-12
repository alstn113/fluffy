package com.pass.auth.application;

import com.pass.auth.application.dto.MyInfoResponse;
import com.pass.auth.domain.Member;
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
