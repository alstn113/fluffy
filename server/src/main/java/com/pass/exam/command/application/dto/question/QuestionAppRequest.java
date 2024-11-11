package com.pass.exam.command.application.dto.question;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = ShortAnswerQuestionAppRequest.class, name = "SHORT_ANSWER"),
        @JsonSubTypes.Type(value = LongAnswerQuestionAppRequest.class, name = "LONG_ANSWER"),
        @JsonSubTypes.Type(value = SingleChoiceQuestionAppRequest.class, name = "SINGLE_CHOICE"),
        @JsonSubTypes.Type(value = MultipleChoiceAppRequest.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = TrueOrFalseQuestionAppRequest.class, name = "TRUE_OR_FALSE")
})
public interface QuestionAppRequest {

    String text();

    String type();
}
