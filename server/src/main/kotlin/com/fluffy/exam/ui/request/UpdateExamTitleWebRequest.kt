package com.fluffy.exam.ui.request

import com.fluffy.exam.application.request.UpdateExamTitleRequest
import com.fluffy.global.web.Accessor

data class UpdateExamTitleWebRequest(val title: String) {

    fun toAppRequest(examId: Long, accessor: Accessor): UpdateExamTitleRequest {
        return UpdateExamTitleRequest(
            examId = examId,
            title = title,
            accessor = accessor,
        )
    }
}