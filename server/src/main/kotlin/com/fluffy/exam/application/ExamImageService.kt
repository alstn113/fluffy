package com.fluffy.exam.application

import com.fluffy.exam.application.request.ExamImagePresignedUrlRequest
import com.fluffy.exam.application.response.ExamImagePresignedUrlResponse
import com.fluffy.exam.domain.ExamImage
import com.fluffy.exam.domain.ExamImageRepository
import com.fluffy.exam.domain.ExamRepository
import com.fluffy.exam.domain.findByIdOrThrow
import com.fluffy.global.exception.ForbiddenException
import com.fluffy.global.storage.StorageClient
import com.fluffy.global.web.Accessor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExamImageService(
    private val storageClient: StorageClient,
    private val examImageRepository: ExamImageRepository,
    private val examRepository: ExamRepository,
) {

    @Transactional
    fun createExamImage(
        examId: Long,
        request: ExamImagePresignedUrlRequest,
        accessor: Accessor,
    ): String {
        validateExamAuthor(examId, accessor)

        val examImage = ExamImage.create(
            examId = examId,
            memberId = accessor.id,
            imageName = request.imageName,
            fileSize = request.fileSize
        )
        examImageRepository.save(examImage)

        return examImage.path
    }

    fun generatePresignedUrl(imagePath: String): ExamImagePresignedUrlResponse {
        val response = storageClient.getPresignedUrl(imagePath)

        return ExamImagePresignedUrlResponse(response.presignedUrl, response.fileUrl)
    }

    private fun validateExamAuthor(examId: Long, accessor: Accessor) {
        val exam = examRepository.findByIdOrThrow(examId)
        val memberId = accessor.id

        if (exam.isNotWrittenBy(memberId)) {
            throw ForbiddenException("시험 작성자만 이미지를 업로드할 수 있습니다.")
        }
    }
}