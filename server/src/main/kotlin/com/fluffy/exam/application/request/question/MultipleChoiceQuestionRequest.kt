package com.fluffy.exam.application.request.question

data class MultipleChoiceQuestionRequest(
    override val text: String,
    override val passage: String,
    override val type: String,
    val options: List<QuestionOptionRequest>,
) : QuestionRequest