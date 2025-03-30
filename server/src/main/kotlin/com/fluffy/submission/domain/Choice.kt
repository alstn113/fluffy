package com.fluffy.submission.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class Choice(

    @field:Column(nullable = false)
    val questionOptionId: Long
)