package com.pass.auth.domain;

import com.pass.global.exception.NotFoundException;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {

    Member save(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findBySocialIdAndProvider(Long socialId, OAuth2Provider provider);

    default Member getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다. 사용자 식별자: " + id));
    }
}
