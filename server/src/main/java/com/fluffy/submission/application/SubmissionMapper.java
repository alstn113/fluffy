package com.fluffy.submission.application;

import com.fluffy.auth.domain.Member;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.Question;
import com.fluffy.exam.domain.QuestionOption;
import com.fluffy.global.exception.BadRequestException;
import com.fluffy.submission.application.request.QuestionResponseRequest;
import com.fluffy.submission.application.request.SubmissionRequest;
import com.fluffy.submission.application.response.SubmissionDetailResponse;
import com.fluffy.submission.application.response.SubmissionDetailResponse.AnswerBaseResponse;
import com.fluffy.submission.application.response.SubmissionDetailResponse.ChoiceAnswerResponse;
import com.fluffy.submission.application.response.SubmissionDetailResponse.ChoiceResponse;
import com.fluffy.submission.application.response.SubmissionDetailResponse.TextAnswerResponse;
import com.fluffy.submission.domain.Answer;
import com.fluffy.submission.domain.Choice;
import com.fluffy.submission.domain.Submission;
import com.fluffy.submission.domain.dto.ParticipantDto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.stereotype.Component;

@Component
public class SubmissionMapper {

    public Submission toSubmission(Exam exam, Long memberId, SubmissionRequest request) {
        List<Question> questions = exam.getQuestionGroup().toList();
        List<QuestionResponseRequest> questionResponseRequests = request.questionResponses();

        validateQuestionSize(questions, questionResponseRequests);

        List<Answer> answers = IntStream.range(0, questions.size())
                .mapToObj(i -> toAnswer(questions.get(i), questionResponseRequests.get(i)))
                .toList();

        return new Submission(request.examId(), memberId, answers);
    }

    private void validateQuestionSize(List<Question> questions, List<QuestionResponseRequest> requests) {
        int questionSize = questions.size();
        int questionResponseRequestSize = requests.size();

        if (questionSize != questionResponseRequestSize) {
            throw new BadRequestException("질문들에 대한 응답의 크기가 일치하지 않습니다. (질문 크기: %s, 응답 크기: %s)"
                    .formatted(questionSize, questionResponseRequestSize));
        }
    }

    private Answer toAnswer(Question question, QuestionResponseRequest request) {
        if (request.answers().isEmpty()) {
            throw new BadRequestException("질문에 대한 응답이 비어 있습니다.");
        }

        return switch (question.getType()) {
            case SHORT_ANSWER, LONG_ANSWER -> toTextAnswer(question, request);
            case SINGLE_CHOICE, TRUE_OR_FALSE -> toSingleChoiceAnswer(question, request);
            case MULTIPLE_CHOICE -> toMultipleChoiceAnswer(question, request);
        };
    }

    public Answer toTextAnswer(Question question, QuestionResponseRequest request) {
        if (request.answers().size() != 1) {
            throw new BadRequestException("텍스트 응답은 하나만 제출할 수 있습니다.");
        }

        return Answer.textAnswer(question.getId(), request.answers().getFirst());
    }

    public Answer toSingleChoiceAnswer(Question question, QuestionResponseRequest request) {
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

    public Answer toMultipleChoiceAnswer(Question question, QuestionResponseRequest request) {
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

    public SubmissionDetailResponse toDetailResponse(Exam exam, Submission submission, Member member) {
        return new SubmissionDetailResponse(
                submission.getId(),
                toDetailAnswers(exam.getQuestionGroup().toList(), submission.getAnswers()),
                new ParticipantDto(
                        member.getId(),
                        member.getName(),
                        member.getEmail(),
                        member.getAvatarUrl()
                ),
                submission.getCreatedAt()
        );
    }

    private List<AnswerBaseResponse> toDetailAnswers(List<Question> questions, List<Answer> answers) {
        return IntStream.range(0, questions.size())
                .mapToObj(i -> {
                    Question question = questions.get(i);
                    Answer answer = answers.get(i);

                    return switch (question.getType()) {
                        case SHORT_ANSWER, LONG_ANSWER -> toDetailTextAnswer(answer, question);
                        case SINGLE_CHOICE, TRUE_OR_FALSE, MULTIPLE_CHOICE -> toDetailChoiceAnswer(answer, question);
                    };
                })
                .toList();
    }

    private AnswerBaseResponse toDetailChoiceAnswer(Answer answer, Question question) {
        return new ChoiceAnswerResponse(
                answer.getId(),
                question.getId(),
                question.getText(),
                question.getType().name(),
                toChoiceResponses(question.getOptionGroup().toList(), answer.getChoices())
        );
    }

    private static AnswerBaseResponse toDetailTextAnswer(Answer answer, Question question) {
        return new TextAnswerResponse(
                answer.getId(),
                question.getId(),
                question.getText(),
                question.getType().name(),
                answer.getText(),
                question.getCorrectAnswer()
        );
    }

    private List<ChoiceResponse> toChoiceResponses(List<QuestionOption> options, List<Choice> choices) {
        return options.stream()
                .map(it -> new ChoiceResponse(
                        it.getId(),
                        it.getText(),
                        it.isCorrect(),
                        choices.stream().anyMatch(choice -> choice.getQuestionOptionId().equals(it.getId()))
                ))
                .toList();
    }
}