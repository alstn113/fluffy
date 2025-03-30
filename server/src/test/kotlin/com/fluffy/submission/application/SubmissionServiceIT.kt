package com.fluffy.submission.application

import com.fluffy.auth.domain.MemberRepository
import com.fluffy.auth.fixture.MemberFixture
import com.fluffy.exam.domain.Exam
import com.fluffy.exam.domain.ExamRepository
import com.fluffy.exam.domain.Question
import com.fluffy.exam.fixture.ExamFixture
import com.fluffy.global.web.Accessor
import com.fluffy.submission.application.request.QuestionResponseRequest
import com.fluffy.submission.application.request.SubmissionRequest
import com.fluffy.submission.domain.SubmissionRepository
import com.fluffy.support.AbstractIntegrationTest
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger

class SubmissionServiceIT(
    private val examRepository: ExamRepository,
    private val submissionRepository: SubmissionRepository,
    private val memberRepository: MemberRepository,
    private val submissionService: SubmissionService,
) : AbstractIntegrationTest() {

    @Test
    fun `한 번만 제출할 수 있는 시험의 경우, 한 번만 제출할 수 있다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())
        val exam = createExam(
            memberId = member.id,
            isSingleAttempt = true,
        )

        val request = SubmissionRequest(
            examId = exam.id,
            questionResponses = listOf(QuestionResponseRequest(answers = listOf("정답"))),
            accessor = Accessor(member.id),
        )

        val counter = AtomicInteger(0)

        runBlocking {
            supervisorScope {  // 하나의 코루틴이 실패해도 다른 코루틴이 계속 실행됨
                val jobs = List(3) {
                    async(Dispatchers.Default) {
                        try {
                            withContext(Dispatchers.IO) {
                                submissionService.submit(request)
                            }
                        } catch (e: Exception) {
                            println("예외 발생: ${e.message}")
                        } finally {
                            counter.incrementAndGet()
                        }
                    }
                }
                jobs.awaitAll()  // 모든 요청이 끝날 때까지 대기
            }
        }

        // then
        val submissions = submissionRepository.findAll()
        submissions.size shouldBe 1
    }


    @Test
    fun `여러 번 제출할 수 있는 시험의 경우, 여러 번 제출할 수 있다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())
        val exam = createExam(
            memberId = member.id,
            isSingleAttempt = false,
        )

        // when
        val request = SubmissionRequest(
            examId = exam.id,
            questionResponses = listOf(QuestionResponseRequest(answers = listOf("정답"))),
            accessor = Accessor(member.id),
        )

        val counter = AtomicInteger(0)

        runBlocking {
            supervisorScope {  // 하나의 코루틴이 실패해도 다른 코루틴이 계속 실행됨
                val jobs = List(3) {
                    async(Dispatchers.Default) {
                        try {
                            withContext(Dispatchers.IO) {
                                submissionService.submit(request)
                            }
                        } catch (e: Exception) {
                            println("예외 발생: ${e.message}")
                        } finally {
                            counter.incrementAndGet()
                        }
                    }
                }
                jobs.awaitAll()  // 모든 요청이 끝날 때까지 대기
            }
        }

        // then
        val submissions = submissionRepository.findAll()
        submissions.size shouldBe 3
    }

    private fun createExam(memberId: Long, isSingleAttempt: Boolean): Exam {
        val exam = ExamFixture.create(memberId = memberId)
        exam.updateQuestions(
            listOf(
                Question.shortAnswer(
                    text = "질문",
                    passage = "지문",
                    correctAnswer = "정답",
                ),
            )
        )
        if (isSingleAttempt) {
            exam.updateIsSingleAttempt(true)
        }
        exam.publish()

        return examRepository.save(exam)
    }
}