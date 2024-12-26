package com.fluffy.auth.domain;

import com.fluffy.global.exception.NotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialIdAndProvider(String socialId, OAuth2Provider provider);

    default Member findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다. 사용자 식별자: " + id));
    }
}
