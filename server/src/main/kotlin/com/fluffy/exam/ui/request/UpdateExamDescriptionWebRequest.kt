package com.fluffy.exam.ui.request

import com.fluffy.exam.application.request.UpdateExamDescriptionRequest
import com.fluffy.global.web.Accessor

data class UpdateExamDescriptionWebRequest(val description: String) {

    fun toAppRequest(examId: Long, accessor: Accessor): UpdateExamDescriptionRequest {
        return UpdateExamDescriptionRequest(
            examId = examId,
            description = description,
            accessor = accessor,
        )
    }
}
