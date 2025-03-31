package com.fluffy.infra.persistence

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

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

//    @PrePersist
//    fun prePersist() {
//        createdAt = createdAt.truncatedTo(ChronoUnit.MICROS)
//        updatedAt = updatedAt.truncatedTo(ChronoUnit.MICROS)
//    }
//
//    @PreUpdate
//    fun preUpdate() {
//        updatedAt = updatedAt.truncatedTo(ChronoUnit.MICROS)
//    }
}
