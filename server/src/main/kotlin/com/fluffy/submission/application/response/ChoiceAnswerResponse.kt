package com.fluffy.submission.application.response

data class ChoiceAnswerResponse(
    override val id: Long,
    override val questionId: Long,
    override val text: String,
    override val type: String,
    val choices: List<ChoiceResponse>
) : AnswerBaseResponse