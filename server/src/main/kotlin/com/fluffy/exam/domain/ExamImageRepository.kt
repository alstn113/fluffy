package com.fluffy.exam.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ExamImageRepository : JpaRepository<ExamImage, UUID>