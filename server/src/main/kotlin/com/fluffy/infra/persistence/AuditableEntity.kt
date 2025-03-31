package com.fluffy.infra.persistence

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AuditableEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.MIN.truncatedTo(ChronoUnit.MICROS)
        protected set

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.MIN.truncatedTo(ChronoUnit.MICROS)
        protected set
}
