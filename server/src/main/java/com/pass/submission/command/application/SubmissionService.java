package com.pass.submission.command.application;

import com.pass.auth.domain.Member;
import com.pass.auth.domain.MemberRepository;
import com.pass.exam.command.domain.Exam;
import com.pass.exam.command.domain.ExamRepository;
import com.pass.exam.command.domain.Question;
import com.pass.exam.command.domain.QuestionOption;
import com.pass.exam.command.domain.QuestionType;
import com.pass.submission.command.application.dto.SubmitAppRequest;
import com.pass.submission.command.application.exception.InvalidAnswerException;
import com.pass.submission.command.application.exception.InvalidChoiceException;
import com.pass.submission.command.application.exception.InvalidTextAnswerException;
import com.pass.submission.command.domain.Answer;
import com.pass.submission.command.domain.Choice;
import com.pass.submission.command.domain.Submission;
import com.pass.submission.command.domain.SubmissionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final ExamRepository examRepository;
    private final SubmissionRepository submissionRepository;
    private final MemberRepository memberRepository;
    private final SubmissionMapper submissionMapper;

    public void submit(SubmitAppRequest request) {
        Member member = memberRepository.getById(request.accessor().id());
        Exam exam = examRepository.getById(request.examId());

        Submission submission = submissionMapper.toSubmission(exam.getId(), member.getId(), request);
        validateSubmissionAgainstExam(exam, submission);

        submissionRepository.save(submission);
    }

    public void validateSubmissionAgainstExam(Exam exam, Submission submission) {
        validateExamId(exam, submission);
        validateQuestionSize(exam, submission);

        List<Question> questions = exam.getQuestionGroup().toList();
        List<Answer> answers = submission.getAnswers();

        for (int i = 0; i < questions.size(); i++) {
            validateQuestionAnswer(questions.get(i), answers.get(i));
        }
    }

    private void validateExamId(Exam exam, Submission submission) {
        if (!exam.getId().equals(submission.getExamId())) {
            throw new InvalidAnswerException();
        }
    }

    private void validateQuestionSize(Exam exam, Submission submission) {
        if (exam.getQuestionGroup().size() != submission.getAnswers().size()) {
            throw new InvalidAnswerException();
        }
    }

    private void validateQuestionAnswer(Question question, Answer answer) {
        if (!question.getId().equals(answer.getQuestionId())) {
            throw new InvalidAnswerException();
        }

        if (question.getType().isTextAnswerable() && answer.getText().isBlank()) {
            throw new InvalidTextAnswerException();
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
