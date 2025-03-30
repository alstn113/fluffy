package com.fluffy.exam.application.request.question

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = As.EXISTING_PROPERTY,
    property = "type",
    visible = true
)
@JsonSubTypes(
    JsonSubTypes.Type(value = ShortAnswerQuestionRequest::class, name = "SHORT_ANSWER"),
    JsonSubTypes.Type(value = LongAnswerQuestionRequest::class, name = "LONG_ANSWER"),
    JsonSubTypes.Type(value = SingleChoiceQuestionRequest::class, name = "SINGLE_CHOICE"),
    JsonSubTypes.Type(value = MultipleChoiceQuestionRequest::class, name = "MULTIPLE_CHOICE"),
    JsonSubTypes.Type(value = TrueOrFalseQuestionRequest::class, name = "TRUE_OR_FALSE")
)
interface QuestionRequest {

    val text: String
    val type: String
    val passage: String
}
