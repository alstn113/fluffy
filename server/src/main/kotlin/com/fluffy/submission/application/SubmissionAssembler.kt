package com.fluffy.submission.application

import com.fluffy.auth.domain.Member
import com.fluffy.exam.domain.Exam
import com.fluffy.exam.domain.Question
import com.fluffy.global.exception.BadRequestException
import com.fluffy.submission.application.request.QuestionResponseRequest
import com.fluffy.submission.application.request.SubmissionRequest
import com.fluffy.submission.application.response.SubmissionDetailResponse
import com.fluffy.submission.domain.Submission
import com.fluffy.submission.domain.dto.ParticipantDto

object SubmissionAssembler {

    fun toSubmission(exam: Exam, memberId: Long, request: SubmissionRequest): Submission {
        val questions: List<Question> = exam.questions
        val questionResponseRequests: List<QuestionResponseRequest> = request.questionResponses

        validateQuestionSize(questions, questionResponseRequests)

        val answers = questions.indices.map { index ->
            AnswerAssembler.toAnswer(questions[index], questionResponseRequests[index])
        }

        return Submission.create(
            examId = exam.id,
            memberId = memberId,
            answers = answers,
        )
    }

    private fun validateQuestionSize(questions: List<Question>, requests: List<QuestionResponseRequest>) {
        val questionSize = questions.size
        val questionResponseRequestSize = requests.size

        if (questionSize != questionResponseRequestSize) {
            throw BadRequestException(
                "문제들에 대한 응답의 크기가 일치하지 않습니다. " +
                        "문제 크기: $questionSize, 응답 크기: $questionResponseRequestSize",
            )
        }
    }

    fun toDetailResponse(exam: Exam, submission: Submission, member: Member): SubmissionDetailResponse {
        val answers = AnswerAssembler.toDetailAnswers(exam.questions, submission.answers)
        val participant = ParticipantDto(
            id = member.id,
            name = member.name,
            email = member.email,
            avatarUrl = member.avatarUrl,
        )

        return SubmissionDetailResponse(
            id = submission.id,
            participant = participant,
            answers = answers,
            submittedAt = submission.createdAt,
        )
    }
}