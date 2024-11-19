package com.pass.submission.command.domain;

import com.pass.exam.command.domain.Exam;
import com.pass.exam.command.domain.Question;
import com.pass.exam.command.domain.QuestionOption;
import com.pass.exam.command.domain.QuestionType;
import com.pass.submission.command.domain.exception.EmptyTextAnswerException;
import com.pass.submission.command.domain.exception.ExamIdMismatchException;
import com.pass.submission.command.domain.exception.InvalidChoiceException;
import com.pass.submission.command.domain.exception.QuestionIdMismatchException;
import com.pass.submission.command.domain.exception.QuestionSizeMismatchException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubmissionValidator {

    public void validate(Exam exam, Submission submission) {
        validateExamId(exam, submission);
        validateQuestionSize(exam, submission);

        List<Question> questions = exam.getQuestionGroup().toList();
        List<Answer> answers = submission.getAnswers();

        for (int i = 0; i < questions.size(); i++) {
            validateQuestionAnswer(questions.get(i), answers.get(i));
        }
    }

    private void validateExamId(Exam exam, Submission submission) {
        String examId = exam.getId();
        String submissionExamId = submission.getExamId();

        if (!examId.equals(submissionExamId)) {
            throw new ExamIdMismatchException(examId);
        }
    }

    private void validateQuestionSize(Exam exam, Submission submission) {
        int questionSize = exam.getQuestionGroup().size();
        int answerSize = submission.getAnswers().size();

        if (questionSize != answerSize) {
            throw new QuestionSizeMismatchException(questionSize, answerSize);
        }
    }

    private void validateQuestionAnswer(Question question, Answer answer) {
        if (!question.getId().equals(answer.getQuestionId())) {
            throw new QuestionIdMismatchException(question.getId(), answer.getQuestionId());
        }

        if (question.getType().isTextAnswerable() && answer.getText().isBlank()) {
            throw new EmptyTextAnswerException();
        } else {
            validateChoiceAgainstOptions(question.getType(), question.getOptionGroup().toList(), answer.getChoices());
        }
    }

    private void validateChoiceAgainstOptions(
            QuestionType questionType,
            List<QuestionOption> options,
            List<Choice> choices
    ) {
        if (questionType == QuestionType.SINGLE_CHOICE) {
            validateSingleChoice(choices, options);
            return;
        }

        validateMultipleChoice(choices, options);
    }

    private void validateSingleChoice(List<Choice> choices, List<QuestionOption> options) {
        if (choices.size() != 1) {
            throw new InvalidChoiceException();
        }

        Long questionOptionId = choices.getFirst().getQuestionOptionId();

        if (options.stream().noneMatch(option -> option.getId().equals(questionOptionId))) {
            throw new InvalidChoiceException();
        }
    }

    private void validateMultipleChoice(List<Choice> choices, List<QuestionOption> options) {
        List<Long> questionOptionIds = choices.stream()
                .map(Choice::getQuestionOptionId)
                .toList();

        if (options.stream().noneMatch(option -> questionOptionIds.contains(option.getId()))) {
            throw new InvalidChoiceException();
        }
    }
}
