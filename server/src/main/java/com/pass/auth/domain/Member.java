package com.pass.auth.domain;

import com.pass.global.persistence.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OAuth2Provider provider;

    @Column(nullable = false)
    private Long socialId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String imageUrl;

    public Member(String email, OAuth2Provider provider, Long socialId, String name, String imageUrl) {
        this(null, email, provider, socialId, name, imageUrl);
    }

    public Member(Long id, String email, OAuth2Provider provider, Long socialId, String name, String imageUrl) {
        this.id = id;
        this.email = email;
        this.provider = provider;
        this.socialId = socialId;
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
