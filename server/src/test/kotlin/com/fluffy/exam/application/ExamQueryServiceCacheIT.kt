package com.fluffy.exam.application

import com.fluffy.auth.domain.MemberRepository
import com.fluffy.auth.fixture.MemberFixture
import com.fluffy.exam.domain.Exam
import com.fluffy.exam.domain.ExamRepository
import com.fluffy.exam.domain.Question
import com.fluffy.exam.domain.QuestionOption
import com.fluffy.reaction.domain.Reaction
import com.fluffy.reaction.domain.ReactionRepository
import com.fluffy.reaction.domain.ReactionType
import com.fluffy.support.AbstractIntegrationTest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.data.redis.core.RedisTemplate

class ExamQueryServiceCacheIT(
    private val memberRepository: MemberRepository,
    private val examQueryService: ExamQueryService,
    private val reactionRepository: ReactionRepository,
    private val redisTemplate: RedisTemplate<String, String>,
    private val examRepository: ExamRepository,
) : AbstractIntegrationTest() {

    @Test
    fun `시험 상세 정보 조회 시 캐시를 사용한다`() {
        val member = memberRepository.save(MemberFixture.create())
        val exam = createExam(member.id)

        // 아직 캐시가 적용되지 않은 경우
        val key = "examDetail::${exam.id}"
        redisTemplate.hasKey(key) shouldBe false

        // 첫 요청은 DB 에서 조회
        examQueryService.getExamDetail(exam.id)
        redisTemplate.hasKey(key) shouldBe true

        // 다음 요청부터 DB 조회 없이 캐시에서 조회
        examQueryService.getExamDetail(exam.id)
        redisTemplate.hasKey(key) shouldBe true
    }

    private fun createExam(memberId: Long): Exam {
        val exam = Exam.create("시험 제목", memberId)

        exam.updateQuestions(
            listOf(
                Question.shortAnswer("질문 제목", "질문 설명", "정답"),
                Question.singleChoice(
                    "질문 제목", "질문 설명", listOf(
                        QuestionOption("선택지 1", true),
                        QuestionOption("선택지 2", false),
                        QuestionOption("선택지 3", false),
                    )
                )
            )
        )
        exam.publish()
        examRepository.save(exam)

        repeat(3) {
            val reactionMember = memberRepository.save(MemberFixture.create())
            reactionRepository.save(
                Reaction.create(
                    targetType = "EXAM",
                    targetId = exam.id,
                    memberId = reactionMember.id,
                    type = ReactionType.LIKE
                )
            )
        }

        return exam
    }
}