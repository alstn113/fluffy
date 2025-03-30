package com.fluffy.exam.domain

import com.fluffy.infra.persistence.AuditableEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity
class ExamImage private constructor(
    id: UUID,
    memberId: Long,
    examId: Long,
    path: String,
    fileSize: Long,
) : AuditableEntity() {

    @Id
    var id: UUID = id
        protected set

    @Column(nullable = false)
    var memberId: Long = memberId
        protected set

    @Column(nullable = false)
    var examId: Long = examId
        protected set

    @Column(nullable = false)
    var path: String = path
        protected set

    @Column(nullable = false)
    var fileSize: Long = fileSize
        protected set

    companion object {

        fun create(examId: Long, memberId: Long, imageName: String, fileSize: Long): ExamImage {
            val imageId = UUID.randomUUID()
            val imagePath = createImagePath(examId, imageId, imageName)

            return ExamImage(
                id = imageId,
                memberId = memberId,
                examId = examId,
                path = imagePath,
                fileSize = fileSize
            )
        }

        private fun createImagePath(examId: Long, imageId: UUID, fileName: String): String {
            val extension = fileName.substringAfterLast(".")
            return "exams/$examId/$imageId.$extension"
        }
    }
}