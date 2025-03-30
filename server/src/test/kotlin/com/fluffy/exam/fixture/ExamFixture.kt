package com.fluffy.exam.fixture

import com.fluffy.exam.domain.Exam
import com.fluffy.exam.domain.ExamDescription
import com.fluffy.exam.domain.ExamStatus
import com.fluffy.exam.domain.ExamTitle

object ExamFixture {

    fun create(
        id: Long = 0L,
        title: String = "시험 제목",
        description: String = "시험 설명",
        status: ExamStatus = ExamStatus.DRAFT,
        memberId: Long = 1L,
        isSingleAttempt: Boolean = false,
    ) = Exam(
        title = ExamTitle(title),
        description = ExamDescription(description),
        status = status,
        memberId = memberId,
        isSingleAttempt = isSingleAttempt,
        id = id
    )
}