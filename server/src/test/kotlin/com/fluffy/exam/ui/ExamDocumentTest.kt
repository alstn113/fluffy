package com.fluffy.exam.ui

import com.fluffy.exam.application.request.ExamImagePresignedUrlRequest
import com.fluffy.exam.application.request.question.*
import com.fluffy.exam.application.response.*
import com.fluffy.exam.domain.ExamStatus
import com.fluffy.exam.domain.dto.AuthorDto
import com.fluffy.exam.domain.dto.ExamSummaryDto
import com.fluffy.exam.domain.dto.MyExamSummaryDto
import com.fluffy.exam.domain.dto.SubmittedExamSummaryDto
import com.fluffy.exam.ui.request.*
import com.fluffy.global.response.PageInfo
import com.fluffy.global.response.PageResponse
import com.fluffy.support.AbstractDocumentTest
import io.mockk.every
import io.mockk.justRun
import jakarta.servlet.http.Cookie
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

class ExamDocumentTest : AbstractDocumentTest() {

    @Test
    @DisplayName("출제된 시험 요약 목록을 조회할 수 있다.")
    fun getPublishedExamSummaries() {
        val pageInfo = PageInfo(0, 2, 4, true, false)
        val summaries = listOf(
            ExamSummaryDto(
                1L, "시험1", "설명1", ExamStatus.PUBLISHED,
                AuthorDto(1L, "작성자1", "a@gmail.com"),
                3L, 2L, LocalDateTime.now(), LocalDateTime.now()
            ),
            ExamSummaryDto(
                2L, "시험2", "설명2", ExamStatus.PUBLISHED,
                AuthorDto(2L, "작성자2", "b@gmail.com"),
                3L, 1L, LocalDateTime.now(), LocalDateTime.now()
            )
        )
        val response = PageResponse(pageInfo, summaries)

        every { examQueryService.getPublishedExamSummaries(any()) } returns response

        mockMvc.perform(
            get("/api/v1/exams")
                .param("page", "0")
                .param("size", "2")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpectAll(
                status().isOk,
                content().json(objectMapper.writeValueAsString(response))
            )
            .andDo(
                restDocs.document(
                    queryParameters(
                        parameterWithName("page").description("페이지 번호").optional(),
                        parameterWithName("size").description("페이지당 항목 수").optional()
                    ),
                    responseFields(
                        fieldWithPath("pageInfo").description("페이지 정보"),
                        fieldWithPath("pageInfo.currentPage").description("현재 페이지 번호"),
                        fieldWithPath("pageInfo.totalPages").description("전체 페이지 수"),
                        fieldWithPath("pageInfo.totalElements").description("전체 항목 수"),
                        fieldWithPath("pageInfo.hasNext").description("다음 페이지 존재 여부"),
                        fieldWithPath("pageInfo.hasPrevious").description("이전 페이지 존재 여부"),

                        fieldWithPath("content").description("내용"),
                        fieldWithPath("content[].id").description("시험 ID"),
                        fieldWithPath("content[].title").description("시험 제목"),
                        fieldWithPath("content[].description").description("시험 설명"),
                        fieldWithPath("content[].status").description("시험 상태"),
                        fieldWithPath("content[].author").description("작성자 정보"),
                        fieldWithPath("content[].questionCount").description("문제 수"),
                        fieldWithPath("content[].likeCount").description("좋아요 수"),
                        fieldWithPath("content[].createdAt").description("생성일"),
                        fieldWithPath("content[].updatedAt").description("수정일"),
                        fieldWithPath("content[].author.id").description("작성자 ID"),
                        fieldWithPath("content[].author.name").description("작성자 이름"),
                        fieldWithPath("content[].author.avatarUrl").description("작성자 아바타 URL")
                    )
                )
            )
    }

    @Test
    @DisplayName("내가 출제한 시험 요약 목록을 조회할 수 있다.")
    fun getMyExamSummaries() {
        val pageInfo = PageInfo(0, 2, 4, true, false)
        val summaries = listOf(
            MyExamSummaryDto(
                1L, "시험1", "설명1", ExamStatus.PUBLISHED,
                AuthorDto(1L, "작성자1", "a@gmail.com"),
                3L, LocalDateTime.now(), LocalDateTime.now()
            ),
            MyExamSummaryDto(
                2L, "시험2", "설명2", ExamStatus.PUBLISHED,
                AuthorDto(1L, "작성자1", "a@gmail.com"),
                2L, LocalDateTime.now(), LocalDateTime.now()
            )
        )
        val response = PageResponse(pageInfo, summaries)

        every { examQueryService.getMyExamSummaries(any(), any(), any()) } returns response

        mockMvc.perform(
            get("/api/v1/exams/mine")
                .param("page", "0")
                .param("size", "2")
                .accept(MediaType.APPLICATION_JSON)
                .cookie(Cookie("accessToken", "{ACCESS_TOKEN}"))
        )
            .andExpect(status().isOk)
            .andDo(
                restDocs.document(
                    queryParameters(
                        parameterWithName("page").description("페이지 번호").optional(),
                        parameterWithName("size").description("페이지당 항목 수").optional()
                    ),
                    responseFields(
                        fieldWithPath("pageInfo").description("페이지 정보"),
                        fieldWithPath("pageInfo.currentPage").description("현재 페이지 번호"),
                        fieldWithPath("pageInfo.totalPages").description("전체 페이지 수"),
                        fieldWithPath("pageInfo.totalElements").description("전체 항목 수"),
                        fieldWithPath("pageInfo.hasNext").description("다음 페이지 존재 여부"),
                        fieldWithPath("pageInfo.hasPrevious").description("이전 페이지 존재 여부"),

                        fieldWithPath("content").description("내용"),
                        fieldWithPath("content[].id").description("시험 ID"),
                        fieldWithPath("content[].title").description("시험 제목"),
                        fieldWithPath("content[].description").description("시험 설명"),
                        fieldWithPath("content[].status").description("시험 상태"),
                        fieldWithPath("content[].author").description("작성자 정보"),
                        fieldWithPath("content[].questionCount").description("문제 수"),
                        fieldWithPath("content[].createdAt").description("생성일"),
                        fieldWithPath("content[].updatedAt").description("수정일"),
                        fieldWithPath("content[].author.id").description("작성자 ID"),
                        fieldWithPath("content[].author.name").description("작성자 이름"),
                        fieldWithPath("content[].author.avatarUrl").description("작성자 아바타 URL")
                    )
                )
            )
    }

    @Test
    @DisplayName("시험 상세 요약 정보를 조회할 수 있다.")
    fun getExamDetailSummary() {
        val response = ExamDetailSummaryResponse(
            1L,
            "시험1",
            "설명1",
            ExamStatus.PUBLISHED.name,
            AuthorDto(1L, "작성자1", "https://avatar.com"),
            3L,
            2L,
            true,
            LocalDateTime.now(),
            LocalDateTime.now()
        )

        every { examQueryService.getExamDetailSummary(any(), any()) } returns response

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/v1/exams/{examId}/summary", 1)
                .accept(MediaType.APPLICATION_JSON)
                .cookie(Cookie("accessToken", "{ACCESS_TOKEN}"))
        )
            .andExpectAll(
                status().isOk,
                content().json(objectMapper.writeValueAsString(response))
            )
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("examId").description("시험 ID")
                    ),
                    responseFields(
                        fieldWithPath("id").description("시험 ID"),
                        fieldWithPath("title").description("시험 제목"),
                        fieldWithPath("description").description("시험 설명"),
                        fieldWithPath("status").description("시험 상태"),
                        fieldWithPath("author").description("작성자 정보"),
                        fieldWithPath("author.id").description("작성자 ID"),
                        fieldWithPath("author.name").description("작성자 이름"),
                        fieldWithPath("author.avatarUrl").description("작성자 아바타 URL"),
                        fieldWithPath("questionCount").description("문제 수"),
                        fieldWithPath("likeCount").description("좋아요 수"),
                        fieldWithPath("isLiked").description("좋아요 여부"),
                        fieldWithPath("createdAt").description("생성일"),
                        fieldWithPath("updatedAt").description("수정일")
                    )
                )
            )
    }

    @Test
    @DisplayName("시험 상세 정보를 조회할 수 있다.")
    fun getExamDetail() {
        val response = ExamDetailResponse(
            1L, "시험1", "설명1", ExamStatus.PUBLISHED.name,
            listOf(
                AnswerQuestionResponse(1L, "질문1", "지문", "SHORT_ANSWER"),
                AnswerQuestionResponse(2L, "질문2", "지문", "LONG_ANSWER"),
                ChoiceQuestionResponse(
                    3L, "질문3", "지문", "SINGLE_CHOICE",
                    listOf(QuestionOptionResponse(1L, "선택1"), QuestionOptionResponse(2L, "선택2"))
                ),
                ChoiceQuestionResponse(
                    4L, "질문4", "지문", "MULTIPLE_CHOICE",
                    listOf(QuestionOptionResponse(3L, "선택3"), QuestionOptionResponse(4L, "선택4"))
                ),
                ChoiceQuestionResponse(
                    5L, "질문5", "지문", "TRUE_OR_FALSE",
                    listOf(QuestionOptionResponse(5L, "TRUE"), QuestionOptionResponse(6L, "FALSE"))
                )
            ), LocalDateTime.now(), LocalDateTime.now()
        )

        every { examQueryService.getExamDetail(any()) } returns response

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/v1/exams/{examId}", 1)
                .param("page", "0")
                .param("size", "2")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpectAll(
                status().isOk,
                content().json(objectMapper.writeValueAsString(response))
            )
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("examId").description("시험 ID")
                    ),
                    responseFields(
                        fieldWithPath("id").description("시험 ID"),
                        fieldWithPath("title").description("시험 제목"),
                        fieldWithPath("description").description("시험 설명"),
                        fieldWithPath("status").description("시험 상태"),
                        fieldWithPath("questions").description("문제 목록"),
                        fieldWithPath("questions[].id").description("문제 ID"),
                        fieldWithPath("questions[].text").description("문제 내용"),
                        fieldWithPath("questions[].passage").description("문제 지문"),
                        fieldWithPath("questions[].type").description("문제 유형"),
                        fieldWithPath("questions[].options").description("선택지 목록").optional(),
                        fieldWithPath("questions[].options[].id").description("선택지 ID").optional(),
                        fieldWithPath("questions[].options[].text").description("선택지 내용").optional(),
                        fieldWithPath("createdAt").description("생성일"),
                        fieldWithPath("updatedAt").description("수정일")
                    )
                )
            )
    }

    @Test
    @DisplayName("시험 상세 정보와 답안을 함께 조회할 수 있다.")
    fun getExamDetailWithAnswers() {
        val response = ExamWithAnswersResponse(
            1L, "시험1", "설명1", ExamStatus.PUBLISHED.name,
            listOf(
                AnswerQuestionWithAnswersResponse(
                    1L, "질문1", "지문", "SHORT_ANSWER",
                    "정답1"
                ),
                AnswerQuestionWithAnswersResponse(
                    2L, "질문2", "지문", "LONG_ANSWER",
                    "정답2"
                ),
                ChoiceQuestionWithAnswersResponse(
                    3L, "질문3", "지문", "SINGLE_CHOICE",
                    listOf(
                        QuestionOptionWithAnswersResponse(
                            1L, "선택1", true
                        ),
                        QuestionOptionWithAnswersResponse(
                            2L, "선택2", false
                        )
                    )
                ),
                ChoiceQuestionWithAnswersResponse(
                    4L, "질문4", "지문",
                    "MULTIPLE_CHOICE",
                    listOf(
                        QuestionOptionWithAnswersResponse(
                            3L, "선택1", true
                        ),
                        QuestionOptionWithAnswersResponse(
                            4L, "선택2", true
                        )
                    )
                ),
                ChoiceQuestionWithAnswersResponse(
                    5L, "질문5", "지문", "TRUE_OR_FALSE",
                    listOf(
                        QuestionOptionWithAnswersResponse(
                            5L, "TRUE", true
                        ),
                        QuestionOptionWithAnswersResponse(
                            6L, "FALSE", false
                        )
                    )
                )
            ), LocalDateTime.now(), LocalDateTime.now()
        )

        every { examQueryService.getExamWithAnswers(any(), any()) } returns response

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/v1/exams/{examId}/with-answers", 1)
                .param("page", "0")
                .param("size", "2")
                .accept(MediaType.APPLICATION_JSON)
                .cookie(Cookie("accessToken", "{ACCESS_TOKEN}"))
        )
            .andExpectAll(
                status().isOk,
                content().json(objectMapper.writeValueAsString(response))
            )
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("examId").description("시험 ID")
                    ),
                    responseFields(
                        fieldWithPath("id").description("시험 ID"),
                        fieldWithPath("title").description("시험 제목"),
                        fieldWithPath("description").description("시험 설명"),
                        fieldWithPath("status").description("시험 상태"),
                        fieldWithPath("questions").description("문제 목록"),
                        fieldWithPath("questions[].id").description("문제 ID"),
                        fieldWithPath("questions[].text").description("문제 내용"),
                        fieldWithPath("questions[].passage").description("문제 지문"),
                        fieldWithPath("questions[].type").description("문제 유형"),
                        fieldWithPath("questions[].correctAnswer").description("정답").optional(),
                        fieldWithPath("questions[].options").description("선택지 목록").optional(),
                        fieldWithPath("questions[].options[].id").description("선택지 ID").optional(),
                        fieldWithPath("questions[].options[].text").description("선택지 내용").optional(),
                        fieldWithPath("questions[].options[].isCorrect").description("정답 여부").optional(),
                        fieldWithPath("createdAt").description("생성일"),
                        fieldWithPath("updatedAt").description("수정일")
                    )
                )
            )
    }

    @Test
    @DisplayName("내가 제출한 시험 요약 목록을 조회할 수 있다.")
    fun getSubmittedExamSummaries() {
        val pageInfo = PageInfo(0, 1, 1, false, false)
        val summaries = listOf(
            SubmittedExamSummaryDto(
                1L,
                "시험1",
                "설명1",
                AuthorDto(1L, "작성자1", "https://avatar.com"),
                3L,
                LocalDateTime.now()
            )
        )
        val response = PageResponse(pageInfo, summaries)

        every { examQueryService.getSubmittedExamSummaries(any(), any()) } returns response

        mockMvc.perform(
            get("/api/v1/exams/submitted")
                .param("page", "0")
                .param("size", "9")
                .cookie(Cookie("accessToken", "{ACCESS_TOKEN}"))
        )
            .andExpectAll(
                status().isOk,
                content().json(objectMapper.writeValueAsString(response))
            )
            .andDo(
                restDocs.document(
                    queryParameters(
                        parameterWithName("page").description("페이지 번호").optional(),
                        parameterWithName("size").description("페이지당 항목 수").optional()
                    ),
                    responseFields(
                        fieldWithPath("pageInfo").description("페이지 정보"),
                        fieldWithPath("pageInfo.currentPage").description("현재 페이지 번호"),
                        fieldWithPath("pageInfo.totalPages").description("전체 페이지 수"),
                        fieldWithPath("pageInfo.totalElements").description("전체 항목 수"),
                        fieldWithPath("pageInfo.hasNext").description("다음 페이지 존재 여부"),
                        fieldWithPath("pageInfo.hasPrevious").description("이전 페이지 존재 여부"),

                        fieldWithPath("content").description("내용"),
                        fieldWithPath("content[].examId").description("시험 ID"),
                        fieldWithPath("content[].title").description("시험 제목"),
                        fieldWithPath("content[].description").description("시험 설명"),
                        fieldWithPath("content[].author").description("작성자 정보"),
                        fieldWithPath("content[].submissionCount").description("제출 수"),
                        fieldWithPath("content[].lastSubmissionDate").description("마지막 제출일"),
                        fieldWithPath("content[].author.id").description("작성자 ID"),
                        fieldWithPath("content[].author.name").description("작성자 이름"),
                        fieldWithPath("content[].author.avatarUrl").description("작성자 아바타 URL")
                    )
                )
            )
    }

    @Test
    @DisplayName("시험을 생성할 수 있다.")
    fun createExam() {
        val request = CreateExamWebRequest("시험 제목")
        val response = CreateExamResponse(1L, "시험 제목")

        every { examService.create(any()) } returns response

        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/v1/exams")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(Cookie("accessToken", "{ACCESS_TOKEN}"))
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpectAll(
                status().isCreated,
                header().string("Location", "/api/v1/exams/1"),
                content().json(objectMapper.writeValueAsString(response))
            )
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("title").description("시험 제목")
                    ),
                    responseFields(
                        fieldWithPath("id").description("시험 ID"),
                        fieldWithPath("title").description("시험 제목")
                    )
                )
            )
    }

    @Test
    @DisplayName("시험을 출제할 수 있다.")
    fun publish() {
        val request = PublishExamWebRequest(
            listOf(
                ShortAnswerQuestionRequest("질문1", "지문", "SHORT_ANSWER", "답"),
                LongAnswerQuestionRequest("질문2", "지문", "LONG_ANSWER"),
                SingleChoiceQuestionRequest(
                    "질문3", "지문", "SINGLE_CHOICE",
                    listOf(
                        QuestionOptionRequest("선택1", false),
                        QuestionOptionRequest("선택2", true)
                    )
                ),
                MultipleChoiceQuestionRequest(
                    "질문4", "지문", "MULTIPLE_CHOICE",
                    listOf(
                        QuestionOptionRequest("선택1", true),
                        QuestionOptionRequest("선택2", true)
                    )
                ),
                TrueOrFalseQuestionRequest("질문5", "지문", "TRUE_OR_FALSE", true)
            )
        )

        justRun { examService.publish(any()) }

        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/v1/exams/{examId}/publish", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(Cookie("accessToken", "{ACCESS_TOKEN}"))
                .content(objectMapper.writeValueAsString(request))
        )
            .andDo(print())
            .andExpectAll(
                status().isOk
            )
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("examId").description("시험 ID")
                    ),
                    requestFields(
                        fieldWithPath("questions").description("문제 목록"),
                        fieldWithPath("questions[].text").description("문제 내용"),
                        fieldWithPath("questions[].passage").description("문제 지문").optional(),
                        fieldWithPath("questions[].type").description("문제 유형"),
                        fieldWithPath("questions[].correctAnswer").description("정답").optional(),
                        fieldWithPath("questions[].trueOrFalse").description("true or false 정답").optional(),
                        fieldWithPath("questions[].options").description("선택지 목록").optional(),
                        fieldWithPath("questions[].options[].text").description("선택지 내용").optional(),
                        fieldWithPath("questions[].options[].isCorrect").description("정답 여부").optional()
                    )
                )
            )
    }

    @Test
    @DisplayName("시험 이미지에 대한 Presigend URL을 생성할 수 있다.")
    fun createPresignedUrl() {
        val request = ExamImagePresignedUrlRequest("BLA-BLA.png", 1024L)
        val response = ExamImagePresignedUrlResponse(
            "https://bucket-s3.com/exams/1/BLA-BLA.png",
            "https://cdn.fluffy.run/exams/1/BLA-BLA.png"
        )

        every { examImageService.createExamImage(any(), any(), any()) } returns "exams/1/BLA-BLA.png"
        every { examImageService.generatePresignedUrl(any()) } returns response

        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/v1/exams/{examId}/images/presigned-url", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(Cookie("accessToken", "{ACCESS_TOKEN}"))
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpectAll(
                status().isOk,
                content().json(objectMapper.writeValueAsString(response))
            )
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("examId").description("시험 ID")
                    ),
                    requestFields(
                        fieldWithPath("imageName").description("파일 이름"),
                        fieldWithPath("fileSize").description("파일 크기")
                    ),
                    responseFields(
                        fieldWithPath("presignedUrl").description("Presigned URL"),
                        fieldWithPath("imageUrl").description("이미지 URL")
                    )
                )
            )
    }

    @Test
    @DisplayName("시험 문제를 수정할 수 있다.")
    fun updateQuestions() {
        val request = UpdateExamQuestionsWebRequest(
            listOf(
                ShortAnswerQuestionRequest("질문1", "지문", "SHORT_ANSWER", "답"),
                LongAnswerQuestionRequest("질문2", "지문", "LONG_ANSWER"),
                SingleChoiceQuestionRequest(
                    "질문3", "지문", "SINGLE_CHOICE",
                    listOf(
                        QuestionOptionRequest("선택1", false),
                        QuestionOptionRequest("선택2", true)
                    )
                ),
                MultipleChoiceQuestionRequest(
                    "질문4", "지문", "MULTIPLE_CHOICE",
                    listOf(
                        QuestionOptionRequest("선택1", true),
                        QuestionOptionRequest("선택2", true)
                    )
                ),
                TrueOrFalseQuestionRequest("질문5", "지문", "TRUE_OR_FALSE", true)
            )
        )

        justRun { examService.updateQuestions(any()) }

        mockMvc.perform(
            RestDocumentationRequestBuilders.put("/api/v1/exams/{examId}/questions", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(Cookie("accessToken", "{ACCESS_TOKEN}"))
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpectAll(
                status().isNoContent
            )
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("examId").description("시험 ID")
                    ),
                    requestFields(
                        fieldWithPath("questions").description("문제 목록"),
                        fieldWithPath("questions[].text").description("문제 내용"),
                        fieldWithPath("questions[].passage").description("문제 지문"),
                        fieldWithPath("questions[].type").description("문제 유형"),
                        fieldWithPath("questions[].correctAnswer").description("정답").optional(),
                        fieldWithPath("questions[].trueOrFalse").description("true or false 정답").optional(),
                        fieldWithPath("questions[].options").description("선택지 목록").optional(),
                        fieldWithPath("questions[].options[].text").description("선택지 내용").optional(),
                        fieldWithPath("questions[].options[].isCorrect").description("정답 여부").optional()
                    )
                )
            )
    }

    @Test
    @DisplayName("시험 제목을 수정할 수 있다.")
    fun updateTitle() {
        val request = UpdateExamTitleWebRequest("시험 제목")

        justRun { examService.updateTitle(any()) }

        mockMvc.perform(
            RestDocumentationRequestBuilders.patch("/api/v1/exams/{examId}/title", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(Cookie("accessToken", "{ACCESS_TOKEN}"))
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("examId").description("시험 ID")
                    ),
                    requestFields(
                        fieldWithPath("title").description("시험 제목")
                    )
                )
            )
    }

    @Test
    @DisplayName("시험 설명을 수정할 수 있다.")
    fun updateDescription() {
        val request = UpdateExamDescriptionWebRequest("시험 설명")

        justRun { examService.updateDescription(any()) }

        mockMvc.perform(
            RestDocumentationRequestBuilders.patch("/api/v1/exams/{examId}/description", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(Cookie("accessToken", "{ACCESS_TOKEN}"))
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("examId").description("시험 ID")
                    ),
                    requestFields(
                        fieldWithPath("description").description("시험 설명")
                    )
                )
            )
    }
}