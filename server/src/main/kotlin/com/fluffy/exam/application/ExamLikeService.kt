package com.fluffy.exam.application

import com.fluffy.global.web.Accessor
import com.fluffy.reaction.domain.Like
import com.fluffy.reaction.domain.LikeService
import com.fluffy.reaction.domain.LikeTarget
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExamLikeService(
    private val likeService: LikeService
) {

    @Transactional
    fun like(examId: Long, accessor: Accessor): Long {
        val like = Like(LikeTarget.EXAM, examId)
        val memberId = accessor.id

        return likeService.like(like, memberId)
    }

    @Transactional
    fun unlike(examId: Long, accessor: Accessor): Long {
        val like = Like(LikeTarget.EXAM, examId)
        val memberId = accessor.id

        return likeService.removeLike(like, memberId)
    }
}