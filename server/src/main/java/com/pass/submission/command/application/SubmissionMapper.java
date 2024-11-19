package com.pass.submission.command.application;

import com.pass.submission.command.application.dto.AnswerAppRequest;
import com.pass.submission.command.application.dto.ChoiceAppRequest;
import com.pass.submission.command.application.dto.SubmitAppRequest;
import com.pass.submission.command.application.exception.InvalidAnswerException;
import com.pass.submission.command.domain.Answer;
import com.pass.submission.command.domain.Choice;
import com.pass.submission.command.domain.Submission;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class SubmissionMapper {

    public Submission toSubmission(String examId, Long memberId, SubmitAppRequest request) {
        return new Submission(examId, memberId, toAnswers(request.answers()));
    }

    public List<Answer> toAnswers(List<AnswerAppRequest> requests) {
        return requests.stream()
                .map(this::toAnswer)
                .toList();
    }

    public Answer toAnswer(AnswerAppRequest request) {
        Optional<String> text = Optional.ofNullable(request.text());
        Optional<List<ChoiceAppRequest>> choices = Optional.ofNullable(request.choices());

        if (text.isPresent() && choices.isEmpty()) {
            return Answer.textAnswer(request.questionId(), text.get());
        }

        if (text.isEmpty() && choices.isPresent()) {
            return Answer.choiceAnswer(request.questionId(), toChoices(choices.get()));
        }

        throw new InvalidAnswerException();
    }

    public List<Choice> toChoices(List<ChoiceAppRequest> requests) {
        return requests.stream()
                .map(this::toChoice)
                .toList();
    }

    public Choice toChoice(ChoiceAppRequest request) {
        return new Choice(request.questionOptionId());
    }
}
