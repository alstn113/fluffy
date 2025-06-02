package com.fluffy.infra.persistence

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AuditableEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP(6)")
    var createdAt: LocalDateTime = LocalDateTime.MIN
        protected set

    @LastModifiedDate
    @Column(nullable = false, columnDefinition = "TIMESTAMP(6)")
    var updatedAt: LocalDateTime = LocalDateTime.MIN
        protected set

    @PrePersist
    fun prePersist() {
        createdAt = truncateToMicros(createdAt)
        updatedAt = truncateToMicros(updatedAt)
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = truncateToMicros(updatedAt)
    }

    fun updateTimestamp() {
        updatedAt = truncateToMicros(LocalDateTime.now())
    }

    private fun truncateToMicros(dateTime: LocalDateTime): LocalDateTime {
        return dateTime.truncatedTo(ChronoUnit.MICROS)
    }
}
