package com.fluffy.exam.application

import com.fluffy.exam.application.request.ExamImagePresignedUrlRequest
import com.fluffy.exam.application.response.ExamImagePresignedUrlResponse
import com.fluffy.exam.domain.*
import com.fluffy.global.exception.ForbiddenException
import com.fluffy.global.exception.NotFoundException
import com.fluffy.global.storage.StorageClient
import com.fluffy.global.storage.response.PresignedUrlResponse
import com.fluffy.global.web.Accessor
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.springframework.data.repository.findByIdOrNull

class ExamImageServiceTest : BehaviorSpec({

    val storageClient = mockk<StorageClient>()
    val examRepository = mockk<ExamRepository>()
    val examImageRepository = mockk<ExamImageRepository>()
    val examImageService = ExamImageService(
        storageClient = storageClient,
        examRepository = examRepository,
        examImageRepository = examImageRepository
    )

    Given("createExamImage") {
        val examId = 1L
        val request = ExamImagePresignedUrlRequest(imageName = "image.jpg", fileSize = 1024L)
        val memberId = 1L
        val accessor = Accessor(memberId)

        When("시험이 없을 경우") {
            every { examRepository.findByIdOrNull(any()) } returns null

            Then("예외를 발생시킨다.") {
                shouldThrow<NotFoundException> {
                    examImageService.createExamImage(examId, request, accessor)
                }.message shouldBe "존재하지 않는 시험입니다. 시험 식별자: $examId"
            }
        }

        When("시험이 있을 경우") {
            val exam = mockk<Exam>()
            every { examRepository.findByIdOrThrow(any()) } returns exam

            And("시험 작성자가 아닐 경우") {
                every { exam.isNotWrittenBy(memberId) } returns true

                Then("예외를 발생시킨다.") {
                    shouldThrow<ForbiddenException> {
                        examImageService.createExamImage(examId, request, accessor)
                    }.message shouldBe "시험 작성자만 이미지를 업로드할 수 있습니다."
                }
            }

            And("시험 작성자일 경우") {
                mockkObject(ExamImage)

                val examImage = mockk<ExamImage>()
                val expected = "exam/$examId/UUID-1234-5678/image.jpg"
                every { examImage.path } returns expected
                every { ExamImage.create(any(), any(), any(), any()) } returns examImage

                every { exam.isNotWrittenBy(memberId) } returns false
                every { examImageRepository.save(any()) } returns examImage

                val actual = examImageService.createExamImage(examId, request, accessor)

                Then("시험 이미지를 생성하고, 경로를 반환한다.") {
                    actual shouldBe expected
                }

                unmockkObject(ExamImage)
            }
        }
    }

    Given("generatePresignedUrl") {
        val imagePath = "exam/1/UUID-1234-5678/image.jpg"

        When("이미지 경로를 받으면") {
            val presignedUrl = "https://s3-bucket.com/$imagePath"
            val fileUrl = "https://cdn.example.com/$imagePath"
            val response = PresignedUrlResponse(
                presignedUrl = presignedUrl,
                fileUrl = fileUrl
            )
            every { storageClient.getPresignedUrl(any()) } returns response

            val actual = examImageService.generatePresignedUrl(imagePath)
            val expected = ExamImagePresignedUrlResponse(
                presignedUrl = presignedUrl,
                imageUrl = fileUrl
            )

            Then("presignedUrl과 imageUrl을 반환한다.") {
                actual shouldBe expected
            }
        }
    }
})