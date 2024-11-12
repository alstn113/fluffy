package com.pass.auth.domain;

import com.pass.auth.domain.exception.MemberByIdNotFoundException;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {

    Member save(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findBySocialIdAndProvider(Long socialId, OAuth2Provider provider);

    default Member getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new MemberByIdNotFoundException(id));
    }
}
