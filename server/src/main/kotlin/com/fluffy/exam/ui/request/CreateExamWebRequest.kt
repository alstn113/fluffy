package com.fluffy.exam.ui.request

import com.fluffy.exam.application.request.CreateExamRequest
import com.fluffy.global.web.Accessor

data class CreateExamWebRequest(val title: String) {

    fun toAppRequest(accessor: Accessor): CreateExamRequest {
        return CreateExamRequest(
            title = title,
            accessor = accessor
        )
    }
}
