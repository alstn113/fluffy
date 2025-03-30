package com.fluffy.auth.domain

import com.fluffy.infra.persistence.AuditableEntity
import jakarta.persistence.*


@Entity
class Member(
    email: String?,
    provider: OAuth2Provider,
    socialId: String,
    name: String,
    avatarUrl: String,
    id: Long = 0,
) : AuditableEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = id
        protected set

    @Column
    var email: String? = email
        protected set

    @field:Column(nullable = false, columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    var provider: OAuth2Provider = provider
        protected set

    @field:Column(nullable = false)
    var socialId: String = socialId
        protected set

    @field:Column(nullable = false)
    var name: String = name
        protected set

    @field:Column(nullable = false)
    var avatarUrl: String = avatarUrl
        protected set
}