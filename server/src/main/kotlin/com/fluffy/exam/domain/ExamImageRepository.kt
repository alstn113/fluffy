package com.fluffy.exam.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ExamImageRepository : JpaRepository<ExamImage, UUID>