package com.pass.submission.application;

import com.pass.exam.domain.Exam;
import com.pass.exam.domain.Question;
import com.pass.exam.domain.QuestionOption;
import com.pass.global.exception.BadRequestException;
import com.pass.submission.application.dto.QuestionResponseAppRequest;
import com.pass.submission.application.dto.SubmissionAppRequest;
import com.pass.submission.application.dto.SubmissionResponse;
import com.pass.submission.domain.Answer;
import com.pass.submission.domain.Choice;
import com.pass.submission.domain.Submission;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.stereotype.Component;

@Component
public class SubmissionMapper {

    public Submission toSubmission(Exam exam, Long memberId, SubmissionAppRequest request) {
        List<Question> questions = exam.getQuestionGroup().toList();
        List<QuestionResponseAppRequest> questionResponseRequests = request.questionResponses();

        validateQuestionSize(questions, questionResponseRequests);

        List<Answer> answers = IntStream.range(0, questions.size())
                .mapToObj(i -> toAnswer(questions.get(i), questionResponseRequests.get(i)))
                .toList();

        return new Submission(request.examId(), memberId, answers);
    }

    private void validateQuestionSize(List<Question> questions, List<QuestionResponseAppRequest> requests) {
        int questionSize = questions.size();
        int questionResponseRequestSize = requests.size();

        if (questionSize != questionResponseRequestSize) {
            throw new BadRequestException("질문들에 대한 응답의 크기가 일치하지 않습니다. (질문 크기: %s, 응답 크기: %s)"
                    .formatted(questionSize, questionResponseRequestSize));
        }
    }

    private Answer toAnswer(Question question, QuestionResponseAppRequest request) {
        if (request.answers().isEmpty()) {
            throw new BadRequestException("질문에 대한 응답이 비어 있습니다.");
        }

        return switch (question.getType()) {
            case SHORT_ANSWER, LONG_ANSWER -> toTextAnswer(question, request);
            case SINGLE_CHOICE, TRUE_OR_FALSE -> toSingleChoiceAnswer(question, request);
            case MULTIPLE_CHOICE -> toMultipleChoiceAnswer(question, request);
        };
    }

    public Answer toTextAnswer(Question question, QuestionResponseAppRequest request) {
        if (request.answers().size() != 1) {
            throw new BadRequestException("텍스트 응답은 하나만 제출할 수 있습니다.");
        }

        return Answer.textAnswer(question.getId(), request.answers().getFirst());
    }

    public Answer toSingleChoiceAnswer(Question question, QuestionResponseAppRequest request) {
        if (request.answers().size() != 1) {
            throw new BadRequestException("응답은 하나만 제출할 수 있습니다.");
        }

        List<QuestionOption> options = question.getOptionGroup().toList();
        String option = request.answers().getFirst();

        QuestionOption questionOption = options.stream()
                .filter(it -> it.getText().equals(option))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("존재하지 않는 선택지입니다."));

        return Answer.choiceAnswer(question.getId(), List.of(new Choice(questionOption.getId())));
    }

    public Answer toMultipleChoiceAnswer(Question question, QuestionResponseAppRequest request) {
        List<QuestionOption> options = question.getOptionGroup().toList();
        List<String> choices = request.answers();

        List<Choice> choiceList = new ArrayList<>();
        for (String choice : choices) {
            QuestionOption questionOption = options.stream()
                    .filter(it -> it.getText().equals(choice))
                    .findFirst()
                    .orElseThrow(() -> new BadRequestException("존재하지 않는 선택지입니다."));

            choiceList.add(new Choice(questionOption.getId()));
        }

        return Answer.choiceAnswer(question.getId(), choiceList);
    }
}