package com.pass.exam.application;

import com.pass.exam.application.dto.UpdateExamQuestionsAppRequest;
import com.pass.exam.application.dto.question.request.LongAnswerQuestionAppRequest;
import com.pass.exam.application.dto.question.request.MultipleChoiceAppRequest;
import com.pass.exam.application.dto.question.request.QuestionAppRequest;
import com.pass.exam.application.dto.question.request.QuestionOptionRequest;
import com.pass.exam.application.dto.question.request.ShortAnswerQuestionAppRequest;
import com.pass.exam.application.dto.question.request.SingleChoiceQuestionAppRequest;
import com.pass.exam.application.dto.question.request.TrueOrFalseQuestionAppRequest;
import com.pass.exam.application.dto.question.response.AnswerQuestionResponse;
import com.pass.exam.application.dto.question.response.ChoiceQuestionResponse;
import com.pass.exam.application.dto.question.response.QuestionResponse;
import com.pass.exam.domain.Exam;
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

    public List<Question> toQuestions(UpdateExamQuestionsAppRequest request, Exam exam) {
        List<Question> questions = request.questions().stream()
                .map(it -> toQuestion(it, exam))
                .toList();

        return new ArrayList<>(questions);
    }

    public Question toQuestion(QuestionAppRequest request, Exam exam) {
        QuestionType questionType = QuestionType.from(request.type());

        return switch (questionType) {
            case SHORT_ANSWER -> toQuestion((ShortAnswerQuestionAppRequest) request, exam);
            case LONG_ANSWER -> toQuestion((LongAnswerQuestionAppRequest) request, exam);
            case SINGLE_CHOICE -> toQuestion((SingleChoiceQuestionAppRequest) request, exam);
            case MULTIPLE_CHOICE -> toQuestion((MultipleChoiceAppRequest) request, exam);
            case TRUE_OR_FALSE -> toQuestion((TrueOrFalseQuestionAppRequest) request, exam);
        };
    }

    private Question toQuestion(ShortAnswerQuestionAppRequest request, Exam exam) {
        return Question.shortAnswer(
                request.text(),
                request.correctAnswer(),
                exam
        );
    }

    private Question toQuestion(LongAnswerQuestionAppRequest request, Exam exam) {
        return Question.longAnswer(request.text(), exam);
    }

    private Question toQuestion(SingleChoiceQuestionAppRequest request, Exam exam) {
        List<QuestionOption> questionOptions = toQuestionOptions(request.options());

        return Question.singleChoice(request.text(), exam, questionOptions);
    }

    private Question toQuestion(MultipleChoiceAppRequest request, Exam exam) {
        List<QuestionOption> questionOptions = toQuestionOptions(request.options());

        return Question.multipleChoice(request.text(), exam, questionOptions);
    }

    private Question toQuestion(TrueOrFalseQuestionAppRequest request, Exam exam) {
        return Question.trueOrFalse(request.text(), exam, request.trueOrFalse());
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
}
