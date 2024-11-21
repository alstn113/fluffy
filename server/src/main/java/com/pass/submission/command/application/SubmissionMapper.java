package com.pass.submission.command.application;

import com.pass.exam.command.domain.Exam;
import com.pass.exam.command.domain.Question;
import com.pass.exam.command.domain.QuestionOption;
import com.pass.exam.command.domain.QuestionType;
import com.pass.global.exception.BadRequestException;
import com.pass.submission.command.application.dto.AnswerAppRequest;
import com.pass.submission.command.application.dto.SubmitAppRequest;
import com.pass.submission.command.domain.Answer;
import com.pass.submission.command.domain.Choice;
import com.pass.submission.command.domain.Submission;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.stereotype.Component;

@Component
public class SubmissionMapper {

    public Submission toSubmission(Exam exam, Long memberId, SubmitAppRequest request) {
        List<Question> questions = exam.getQuestionGroup().toList();
        List<AnswerAppRequest> answerRequests = request.answers();

        validateQuestionSize(questions, answerRequests);

        List<Answer> answers = IntStream.range(0, questions.size())
                .mapToObj(i -> toAnswer(questions.get(i), answerRequests.get(i)))
                .toList();

        return new Submission(request.examId(), memberId, answers);
    }

    private void validateQuestionSize(List<Question> questions, List<AnswerAppRequest> answerRequests) {
        int questionSize = questions.size();
        int answerSize = answerRequests.size();

        if (questionSize != answerSize) {
            throw new BadRequestException(
                    "질문들에 대한 응답의 크기가 일치하지 않습니다. (질문 크기: %s, 응답 크기: %s)".formatted(questionSize, answerSize));
        }
    }

    private Answer toAnswer(Question question, AnswerAppRequest answerRequest) {
        if (question.getType().isTextAnswerable()) {
            return Answer.textAnswer(question.getId(), answerRequest.text());
        }

        List<QuestionOption> options = question.getOptionGroup().toList();
        List<Integer> numbers = answerRequest.choices();

        if (question.getType() == QuestionType.SINGLE_CHOICE || question.getType() == QuestionType.TRUE_OR_FALSE) {
            validateSingleChoice(options, numbers);

            return Answer.choiceAnswer(question.getId(), toChoices(options, numbers));
        }

        if (question.getType() == QuestionType.MULTIPLE_CHOICE) {
            validateMultipleChoice(options, numbers);

            return Answer.choiceAnswer(question.getId(), toChoices(options, numbers));
        }

        throw new BadRequestException("지원하지 않는 질문 유형입니다.");
    }

    private List<Choice> toChoices(List<QuestionOption> options, List<Integer> numbers) {
        List<Choice> choices = new ArrayList<>();
        for (int i = 0; i < options.size(); i++) {
            if (numbers.contains(i + 1)) {
                choices.add(new Choice(options.get(i).getId()));
            }
        }
        return choices;
    }

    private void validateSingleChoice(List<QuestionOption> options, List<Integer> numbers) {
        if (numbers.size() != 1) {
            throw new BadRequestException("단일 선택지만 제출할 수 있습니다.");
        }

        int number = numbers.getFirst();

        if (number < 1 || number > options.size()) {
            throw new BadRequestException("범위를 벗어난 선택지가 포함되어 있습니다.");
        }
    }

    private void validateMultipleChoice(List<QuestionOption> options, List<Integer> numbers) {
        if (numbers.size() != numbers.stream().distinct().count()) {
            throw new BadRequestException("중복된 선택지가 포함되어 있습니다.");
        }

        if (numbers.stream().anyMatch(number -> number < 1 || number > options.size())) {
            throw new BadRequestException("범위를 벗어난 선택지가 포함되어 있습니다.");
        }
    }
}
