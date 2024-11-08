package com.pass.auth.domain;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {

    void save(Member member);

    boolean existsByUsername(String username);

    Optional<Member> findByUsername(String username);

    default Member getByUsername(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Username not found"));
    }
}
