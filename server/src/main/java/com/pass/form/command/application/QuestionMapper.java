package com.pass.form.command.application;

import com.pass.form.command.application.dto.PublishFormAppRequest;
import com.pass.form.command.application.dto.question.LongAnswerQuestionAppRequest;
import com.pass.form.command.application.dto.question.MultipleChoiceAppRequest;
import com.pass.form.command.application.dto.question.QuestionAppRequest;
import com.pass.form.command.application.dto.question.ShortAnswerQuestionAppRequest;
import com.pass.form.command.application.dto.question.SingleChoiceQuestionAppRequest;
import com.pass.form.command.application.dto.question.TrueOrFalseQuestionAppRequest;
import com.pass.form.command.domain.Form;
import com.pass.form.command.domain.Question;
import com.pass.form.command.domain.QuestionGroup;
import com.pass.form.command.domain.QuestionOption;
import com.pass.form.command.domain.QuestionType;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionMapper {

    public QuestionGroup toQuestionGroup(PublishFormAppRequest request, Form form) {
        List<Question> questions = request.questions().stream()
                .map(it -> toQuestion(it, form))
                .toList();

        return new QuestionGroup(questions);
    }

    public Question toQuestion(QuestionAppRequest request, Form form) {
        QuestionType questionType = QuestionType.from(request.type());

        return switch (questionType) {
            case SHORT_ANSWER -> toQuestion((ShortAnswerQuestionAppRequest) request, form);
            case LONG_ANSWER -> toQuestion((LongAnswerQuestionAppRequest) request, form);
            case SINGLE_CHOICE -> toQuestion((SingleChoiceQuestionAppRequest) request, form);
            case MULTIPLE_CHOICE -> toQuestion((MultipleChoiceAppRequest) request, form);
            case TRUE_OR_FALSE -> toQuestion((TrueOrFalseQuestionAppRequest) request, form);
        };
    }

    private Question toQuestion(ShortAnswerQuestionAppRequest request, Form form) {
        return Question.shortAnswer(
                request.text(),
                request.correctAnswer(),
                form
        );
    }

    private Question toQuestion(LongAnswerQuestionAppRequest request, Form form) {
        return Question.longAnswer(request.text(), form);
    }

    private Question toQuestion(SingleChoiceQuestionAppRequest request, Form form) {
        List<QuestionOption> questionOptions = toQuestionOptions(
                request.options(),
                List.of(request.correctOptionNumber())
        );

        return Question.singleChoice(request.text(), form, questionOptions);
    }

    private Question toQuestion(MultipleChoiceAppRequest request, Form form) {
        List<QuestionOption> questionOptions = toQuestionOptions(
                request.options(),
                request.correctOptionNumbers()
        );

        return Question.multipleChoice(request.text(), form, questionOptions);
    }

    private Question toQuestion(TrueOrFalseQuestionAppRequest request, Form form) {
        return Question.trueOrFalse(
                request.text(),
                form,
                request.trueText(),
                request.falseText(),
                request.trueOrFalse()
        );
    }

    private List<QuestionOption> toQuestionOptions(List<String> options, List<Integer> correctOptionNumbers) {
        return IntStream.range(0, options.size())
                .mapToObj(i -> new QuestionOption(options.get(i), correctOptionNumbers.contains(i + 1)))
                .toList();
    }
}
