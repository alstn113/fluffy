package com.pass.exam.command.application;

import com.pass.exam.command.application.dto.UpdateExamQuestionsAppRequest;
import com.pass.exam.command.application.dto.question.LongAnswerQuestionAppRequest;
import com.pass.exam.command.application.dto.question.MultipleChoiceAppRequest;
import com.pass.exam.command.application.dto.question.QuestionAppRequest;
import com.pass.exam.command.application.dto.question.QuestionOptionRequest;
import com.pass.exam.command.application.dto.question.ShortAnswerQuestionAppRequest;
import com.pass.exam.command.application.dto.question.SingleChoiceQuestionAppRequest;
import com.pass.exam.command.application.dto.question.TrueOrFalseQuestionAppRequest;
import com.pass.exam.command.domain.Exam;
import com.pass.exam.command.domain.Question;
import com.pass.exam.command.domain.QuestionGroup;
import com.pass.exam.command.domain.QuestionOption;
import com.pass.exam.command.domain.QuestionType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionMapper {

    public QuestionGroup toQuestionGroup(UpdateExamQuestionsAppRequest request, Exam exam) {
        List<Question> questions = request.questions().stream()
                .map(it -> toQuestion(it, exam))
                .toList();

        return new QuestionGroup(questions);
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
        return Question.trueOrFalse(
                request.text(),
                exam,
                request.trueOrFalse()
        );
    }

    private List<QuestionOption> toQuestionOptions(List<QuestionOptionRequest> requests) {
        return requests.stream()
                .map(this::toQuestionOption)
                .toList();
    }

    private QuestionOption toQuestionOption(QuestionOptionRequest request) {
        return new QuestionOption(request.text(), request.isCorrect());
    }
}
