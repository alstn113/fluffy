package com.fluffy.exam.application;

import com.fluffy.exam.application.request.question.LongAnswerQuestionAppRequest;
import com.fluffy.exam.application.request.question.MultipleChoiceAppRequest;
import com.fluffy.exam.application.request.question.QuestionAppRequest;
import com.fluffy.exam.application.request.question.QuestionOptionRequest;
import com.fluffy.exam.application.request.question.ShortAnswerQuestionAppRequest;
import com.fluffy.exam.application.request.question.SingleChoiceQuestionAppRequest;
import com.fluffy.exam.application.request.question.TrueOrFalseQuestionAppRequest;
import com.fluffy.exam.application.response.ExamDetailResponse.AnswerQuestionResponse;
import com.fluffy.exam.application.response.ExamDetailResponse.ChoiceQuestionResponse;
import com.fluffy.exam.application.response.ExamDetailResponse.QuestionResponse;
import com.fluffy.exam.application.response.ExamWithAnswersResponse.AnswerQuestionWithAnswersResponse;
import com.fluffy.exam.application.response.ExamWithAnswersResponse.ChoiceQuestionWithAnswersResponse;
import com.fluffy.exam.application.response.ExamWithAnswersResponse.QuestionWithAnswersResponse;
import com.fluffy.exam.domain.Question;
import com.fluffy.exam.domain.QuestionOption;
import com.fluffy.exam.domain.QuestionType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionMapper {

    private final QuestionOptionMapper questionOptionMapper;

    public List<Question> toQuestions(List<QuestionAppRequest> requests) {
        return requests.stream()
                .map(this::toQuestion)
                .toList();
    }

    public Question toQuestion(QuestionAppRequest request) {
        QuestionType questionType = QuestionType.from(request.type());

        return switch (questionType) {
            case SHORT_ANSWER -> toQuestion((ShortAnswerQuestionAppRequest) request);
            case LONG_ANSWER -> toQuestion((LongAnswerQuestionAppRequest) request);
            case SINGLE_CHOICE -> toQuestion((SingleChoiceQuestionAppRequest) request);
            case MULTIPLE_CHOICE -> toQuestion((MultipleChoiceAppRequest) request);
            case TRUE_OR_FALSE -> toQuestion((TrueOrFalseQuestionAppRequest) request);
        };
    }

    private Question toQuestion(ShortAnswerQuestionAppRequest request) {
        return Question.shortAnswer(
                request.text(),
                request.passage(),
                request.correctAnswer()
        );
    }

    private Question toQuestion(LongAnswerQuestionAppRequest request) {
        return Question.longAnswer(request.text(), request.passage());
    }

    private Question toQuestion(SingleChoiceQuestionAppRequest request) {
        List<QuestionOption> questionOptions = toQuestionOptions(request.options());

        return Question.singleChoice(request.text(), request.passage(), questionOptions);
    }

    private Question toQuestion(MultipleChoiceAppRequest request) {
        List<QuestionOption> questionOptions = toQuestionOptions(request.options());

        return Question.multipleChoice(request.text(), request.passage(), questionOptions);
    }

    private Question toQuestion(TrueOrFalseQuestionAppRequest request) {
        return Question.trueOrFalse(request.text(), request.passage(), request.trueOrFalse());
    }

    private List<QuestionOption> toQuestionOptions(List<QuestionOptionRequest> requests) {
        return requests.stream()
                .map(this::toQuestionOption)
                .toList();
    }

    private QuestionOption toQuestionOption(QuestionOptionRequest request) {
        return new QuestionOption(request.text(), request.isCorrect());
    }

    public List<QuestionResponse> toResponses(List<Question> questions) {
        return questions.stream()
                .map(this::toResponse)
                .toList();
    }

    public QuestionResponse toResponse(Question question) {
        QuestionType questionType = question.getType();
        return switch (questionType) {
            case SHORT_ANSWER, LONG_ANSWER -> toAnswerResponse(question);
            case SINGLE_CHOICE, MULTIPLE_CHOICE, TRUE_OR_FALSE -> toChoiceResponse(question);
        };
    }

    private QuestionResponse toAnswerResponse(Question question) {
        return new AnswerQuestionResponse(
                question.getId(),
                question.getText(),
                question.getPassage(),
                question.getType().name()
        );
    }

    private QuestionResponse toChoiceResponse(Question question) {
        return new ChoiceQuestionResponse(
                question.getId(),
                question.getText(),
                question.getPassage(),
                question.getType().name(),
                questionOptionMapper.toResponses(question.getOptionGroup().toList())
        );
    }

    public List<QuestionWithAnswersResponse> toWithAnswersResponses(List<Question> questions) {
        return questions.stream()
                .map(this::toWithAnswersResponse)
                .toList();
    }

    public QuestionWithAnswersResponse toWithAnswersResponse(Question question) {
        QuestionType questionType = question.getType();
        return switch (questionType) {
            case SHORT_ANSWER, LONG_ANSWER -> toAnswerWithAnswersResponse(question);
            case SINGLE_CHOICE, MULTIPLE_CHOICE, TRUE_OR_FALSE -> toChoiceWithAnswersResponse(question);
        };
    }

    private QuestionWithAnswersResponse toAnswerWithAnswersResponse(Question question) {
        return new AnswerQuestionWithAnswersResponse(
                question.getId(),
                question.getText(),
                question.getPassage(),
                question.getType().name(),
                question.getCorrectAnswer()
        );
    }

    private QuestionWithAnswersResponse toChoiceWithAnswersResponse(Question question) {
        return new ChoiceQuestionWithAnswersResponse(
                question.getId(),
                question.getText(),
                question.getPassage(),
                question.getType().name(),
                questionOptionMapper.toWithAnswersResponses(question.getOptionGroup().toList())
        );
    }
}
