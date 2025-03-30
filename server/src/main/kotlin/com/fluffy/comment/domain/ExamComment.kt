package com.fluffy.comment.domain

import com.fluffy.global.exception.BadRequestException
import com.fluffy.infra.persistence.AuditableEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class ExamComment(
    content: String,
    examId: Long,
    memberId: Long,
    parentCommentId: Long?,
    id: Long = 0,
) : AuditableEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = id
        protected set

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String = content
        protected set

    @Column(name = "exam_id", nullable = false)
    var examId: Long = examId
        protected set

    @Column(name = "member_id", nullable = false)
    var memberId: Long = memberId
        protected set

    @Column(name = "parent_comment_id")
    var parentCommentId: Long? = parentCommentId
        protected set

    @Column
    var deletedAt: LocalDateTime? = null
        protected set

    companion object {

        fun create(content: String, examId: Long, memberId: Long): ExamComment {
            return ExamComment(
                content = content,
                examId = examId,
                memberId = memberId,
                parentCommentId = null
            )
        }
    }

    fun reply(content: String, memberId: Long): ExamComment {
        if (isDeleted()) {
            throw BadRequestException("삭제된 댓글에는 답글을 작성할 수 없습니다.")
        }

        if (isReply()) {
            throw BadRequestException("답글에는 답글을 작성할 수 없습니다.")
        }

        return ExamComment(
            content = content,
            examId = examId,
            memberId = memberId,
            parentCommentId = id
        )
    }

    fun delete() {
        if (isDeleted()) {
            throw BadRequestException("이미 삭제된 댓글입니다.")
        }

        deletedAt = LocalDateTime.now()
    }

    fun isDeleted(): Boolean {
        return deletedAt != null
    }

    fun isReply(): Boolean {
        return parentCommentId != null
    }

    fun isNotWrittenBy(memberId: Long): Boolean {
        return this.memberId != memberId
    }
}