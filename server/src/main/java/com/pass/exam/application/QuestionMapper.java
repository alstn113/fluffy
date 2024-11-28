package com.pass.exam.application;

import com.pass.exam.application.dto.ExamResponse.AnswerQuestionResponse;
import com.pass.exam.application.dto.ExamResponse.ChoiceQuestionResponse;
import com.pass.exam.application.dto.ExamResponse.QuestionResponse;
import com.pass.exam.application.dto.ExamWithAnswersResponse.AnswerQuestionWithAnswersResponse;
import com.pass.exam.application.dto.ExamWithAnswersResponse.ChoiceQuestionWithAnswersResponse;
import com.pass.exam.application.dto.ExamWithAnswersResponse.QuestionWithAnswersResponse;
import com.pass.exam.application.dto.UpdateExamQuestionsAppRequest;
import com.pass.exam.application.dto.question.LongAnswerQuestionAppRequest;
import com.pass.exam.application.dto.question.MultipleChoiceAppRequest;
import com.pass.exam.application.dto.question.QuestionAppRequest;
import com.pass.exam.application.dto.question.QuestionOptionRequest;
import com.pass.exam.application.dto.question.ShortAnswerQuestionAppRequest;
import com.pass.exam.application.dto.question.SingleChoiceQuestionAppRequest;
import com.pass.exam.application.dto.question.TrueOrFalseQuestionAppRequest;
import com.pass.exam.domain.Question;
import com.pass.exam.domain.QuestionOption;
import com.pass.exam.domain.QuestionType;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionMapper {

    private final QuestionOptionMapper questionOptionMapper;

    public List<Question> toQuestions(UpdateExamQuestionsAppRequest request) {
        List<Question> questions = request.questions().stream()
                .map(this::toQuestion)
                .toList();

        return new ArrayList<>(questions);
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
                request.correctAnswer()
        );
    }

    private Question toQuestion(LongAnswerQuestionAppRequest request) {
        return Question.longAnswer(request.text());
    }

    private Question toQuestion(SingleChoiceQuestionAppRequest request) {
        List<QuestionOption> questionOptions = toQuestionOptions(request.options());

        return Question.singleChoice(request.text(), questionOptions);
    }

    private Question toQuestion(MultipleChoiceAppRequest request) {
        List<QuestionOption> questionOptions = toQuestionOptions(request.options());

        return Question.multipleChoice(request.text(), questionOptions);
    }

    private Question toQuestion(TrueOrFalseQuestionAppRequest request) {
        return Question.trueOrFalse(request.text(), request.trueOrFalse());
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
                question.getType().name()
        );
    }

    private QuestionResponse toChoiceResponse(Question question) {
        return new ChoiceQuestionResponse(
                question.getId(),
                question.getText(),
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
                question.getType().name(),
                question.getCorrectAnswer()
        );
    }

    private QuestionWithAnswersResponse toChoiceWithAnswersResponse(Question question) {
        return new ChoiceQuestionWithAnswersResponse(
                question.getId(),
                question.getText(),
                question.getType().name(),
                questionOptionMapper.toWithAnswersResponses(question.getOptionGroup().toList())
        );
    }
}
