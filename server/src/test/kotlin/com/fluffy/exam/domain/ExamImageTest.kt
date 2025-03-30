package com.fluffy.exam.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ExamImageTest : StringSpec({

    "시험 이미지가 정상적으로 생성된다." {
        // given
        val examId = 1L
        val memberId = 1L
        val imageName = "cat.png"
        val fileSize = 1024L

        // when
        val examImage = ExamImage.create(examId, memberId, imageName, fileSize)

        // then
        examImage.examId shouldBe examId
        examImage.memberId shouldBe memberId
        examImage.path shouldBe "exams/1/${examImage.id}.png"
    }
})