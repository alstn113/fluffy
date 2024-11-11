package com.pass.auth.domain;

import com.pass.auth.domain.exception.MemberByIdNotFoundException;
import com.pass.auth.domain.exception.MemberByUsernameNotFoundException;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {

    Member save(Member member);

    Optional<Member> findById(Long id);

    boolean existsByUsername(String username);

    Optional<Member> findByUsername(String username);

    default Member getByUsername(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new MemberByUsernameNotFoundException(username));
    }

    default Member getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new MemberByIdNotFoundException(id));
    }
}
