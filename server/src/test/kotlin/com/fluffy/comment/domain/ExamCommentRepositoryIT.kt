package com.fluffy.comment.domain

import com.fluffy.auth.domain.MemberRepository
import com.fluffy.auth.fixture.MemberFixture
import com.fluffy.comment.fixture.ExamCommentFixture
import com.fluffy.exam.domain.Exam
import com.fluffy.exam.domain.ExamRepository
import com.fluffy.exam.domain.Question
import com.fluffy.exam.fixture.ExamFixture
import com.fluffy.global.exception.NotFoundException
import com.fluffy.support.AbstractIntegrationTest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ExamCommentRepositoryIT(
    private val examCommentRepository: ExamCommentRepository,
    private val examRepository: ExamRepository,
    private val memberRepository: MemberRepository
) : AbstractIntegrationTest() {

    @Test
    fun `댓글 식별자로 댓글을 조회할 수 있다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())
        val exam = createExam(member.id)

        val comment = ExamCommentFixture.create(memberId = member.id, examId = exam.id)
        val savedComment = examCommentRepository.save(comment)

        // when
        val foundComment = examCommentRepository.findByIdOrThrow(savedComment.id)

        // then
        foundComment.id shouldBe savedComment.id
    }

    @Test
    fun `존재하지 않는 댓글 식별자로 댓글을 조회할 경우 예외를 발생시킨다`() {
        // given
        val nonExistentCommentId = -1L

        // when & then
        shouldThrow<NotFoundException> {
            examCommentRepository.findByIdOrThrow(nonExistentCommentId)
        }.message shouldBe "존재하지 않는 댓글입니다. 댓글 식별자: $nonExistentCommentId"
    }

    @Test
    fun `루트 댓글들을 조회할 수 있다`() {
        val member1 = memberRepository.save(MemberFixture.create())
        val member2 = memberRepository.save(MemberFixture.create())
        val exam = createExam(memberId = member1.id)

        /*
        댓글 구조

        root1
            - root1reply1
            - root1reply2
        root2
        root3
            - root3reply1
         */

        val root1 = examCommentRepository.save(
            ExamComment.create(
                memberId = member1.id,
                examId = exam.id,
                content = "root1",
            )
        )
        val root1reply1 = examCommentRepository.save(
            root1.reply(
                memberId = member2.id,
                content = "root1reply1",
            )
        )
        val root1reply2 = examCommentRepository.save(
            root1.reply(
                memberId = member2.id,
                content = "root1reply2",
            )
        )
        val root2 = examCommentRepository.save(
            ExamComment.create(
                memberId = member2.id,
                examId = exam.id,
                content = "root2",
            )
        )
        val root3 = examCommentRepository.save(
            ExamComment.create(
                memberId = member1.id,
                examId = exam.id,
                content = "root3",
            )
        )
        val root3reply1 = examCommentRepository.save(
            root3.reply(
                memberId = member2.id,
                content = "root3reply1",
            )
        )

        // when
        val rootComments = examCommentRepository.findRootComments(exam.id)

        // then
        rootComments.size shouldBe 3

        rootComments[0].id shouldBe root3.id
        rootComments[0].content shouldBe "root3"
        rootComments[0].author.name shouldBe member1.name
        rootComments[0].replyCount shouldBe 1

        rootComments[1].id shouldBe root2.id
        rootComments[1].content shouldBe "root2"
        rootComments[1].author.name shouldBe member2.name
        rootComments[1].replyCount shouldBe 0

        rootComments[2].id shouldBe root1.id
        rootComments[2].content shouldBe "root1"
        rootComments[2].author.name shouldBe member1.name
        rootComments[2].replyCount shouldBe 2
    }

    @Test
    fun `루트 댓글들을 조회 시, 삭제된 댓글이면서 답글이 없는 댓글은 조회되지 않는다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())
        val exam = createExam(memberId = member.id)

        /*
        댓글 구조

        root1(deleted) - 제외
        root2(deleted)
        - root2reply1
        root3
        - root3reply1(deleted)
        */

        val root1 = ExamCommentFixture.create(
            content = "root1",
            memberId = member.id,
            examId = exam.id,
        )
        root1.delete()
        examCommentRepository.save(root1)

        val root2 = ExamCommentFixture.create(
            content = "root2",
            memberId = member.id,
            examId = exam.id,
        )
        examCommentRepository.save(root2)
        val root2reply1 = ExamCommentFixture.create(
            content = "root2reply1",
            memberId = member.id,
            examId = exam.id,
            parentCommentId = root2.id
        )
        examCommentRepository.save(root2reply1)
        root2.delete()
        examCommentRepository.save(root2)

        val root3 = ExamCommentFixture.create(
            content = "root3",
            memberId = member.id,
            examId = exam.id,
        )
        examCommentRepository.save(root3)
        val root3reply1 = ExamCommentFixture.create(
            content = "root3reply1",
            memberId = member.id,
            examId = exam.id,
            parentCommentId = root3.id
        )
        root3reply1.delete()
        examCommentRepository.save(root3reply1)

        // when
        val rootComments = examCommentRepository.findRootComments(exam.id)

        // then
        rootComments.size shouldBe 2

        rootComments[0].id shouldBe root3.id
        rootComments[0].content shouldBe "root3"
        rootComments[0].isDeleted shouldBe false
        rootComments[0].replyCount shouldBe 0

        rootComments[1].id shouldBe root2.id
        rootComments[1].content shouldBe "root2"
        rootComments[1].isDeleted shouldBe true
        rootComments[1].replyCount shouldBe 1
    }

    @Test
    fun `루트 댓글들에 대한 답글들을 조회할 수 있다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())
        val exam = createExam(memberId = member.id)

        /*
        댓글 구조

        root1
            - root1reply1
            - root1reply2
         */

        val rootComment = examCommentRepository.save(
            ExamCommentFixture.create(
                content = "root",
                memberId = member.id,
                examId = exam.id,
            )
        )

        val reply1 = examCommentRepository.save(
            ExamCommentFixture.create(
                content = "reply1",
                memberId = member.id,
                examId = exam.id,
                parentCommentId = rootComment.id
            )
        )
        val reply2 = examCommentRepository.save(
            ExamCommentFixture.create(
                content = "reply2",
                memberId = member.id,
                examId = exam.id,
                parentCommentId = rootComment.id
            )
        )

        // when
        val replies = examCommentRepository.findReplyComments(rootComment.id)

        // then
        replies.size shouldBe 2

        replies[0].id shouldBe reply2.id
        replies[0].content shouldBe "reply2"
        replies[0].author.name shouldBe member.name

        replies[1].id shouldBe reply1.id
        replies[1].content shouldBe "reply1"
        replies[1].author.name shouldBe member.name
    }

    @Test
    fun `루트 댓글에 대한 답글들을 조회 시, 삭제된 답글은 조회되지 않는다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())
        val exam = createExam(memberId = member.id)

        /*
        댓글 구조

        root1
            - root1reply1(deleted)
            - root1reply2
         */

        val rootComment = examCommentRepository.save(
            ExamCommentFixture.create(
                content = "root",
                memberId = member.id,
                examId = exam.id,
            )
        )

        val reply1 = ExamCommentFixture.create(
            content = "reply1",
            memberId = member.id,
            examId = exam.id,
            parentCommentId = rootComment.id
        )
        reply1.delete()
        examCommentRepository.save(reply1)

        val reply2 = examCommentRepository.save(
            ExamCommentFixture.create(
                content = "reply2",
                memberId = member.id,
                examId = exam.id,
                parentCommentId = rootComment.id
            )
        )

        // when
        val replies = examCommentRepository.findReplyComments(rootComment.id)

        // then
        replies.size shouldBe 1

        replies[0].id shouldBe reply2.id
        replies[0].content shouldBe "reply2"
    }

    private fun createExam(memberId: Long): Exam {
        val exam = ExamFixture.create(memberId = memberId)
        exam.updateQuestions(
            listOf(
                Question.shortAnswer(
                    text = "질문",
                    passage = "지문",
                    correctAnswer = "정답"
                )
            )
        )
        exam.publish()

        return examRepository.save(exam)
    }
}