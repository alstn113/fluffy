package com.fluffy.exam.ui;

import com.fluffy.exam.application.request.ExamImagePresignedUrlRequest;
import com.fluffy.exam.application.request.question.LongAnswerQuestionRequest;
import com.fluffy.exam.application.request.question.MultipleChoiceRequest;
import com.fluffy.exam.application.request.question.QuestionOptionRequest;
import com.fluffy.exam.application.request.question.ShortAnswerQuestionRequest;
import com.fluffy.exam.application.request.question.SingleChoiceQuestionRequest;
import com.fluffy.exam.application.request.question.TrueOrFalseQuestionRequest;
import com.fluffy.exam.application.response.CreateExamResponse;
import com.fluffy.exam.application.response.ExamDetailResponse;
import com.fluffy.exam.application.response.ExamDetailResponse.AnswerQuestionResponse;
import com.fluffy.exam.application.response.ExamDetailResponse.ChoiceQuestionResponse;
import com.fluffy.exam.application.response.ExamDetailResponse.ChoiceQuestionResponse.QuestionOptionResponse;
import com.fluffy.exam.application.response.ExamDetailSummaryResponse;
import com.fluffy.exam.application.response.ExamImagePresignedUrlResponse;
import com.fluffy.exam.application.response.ExamWithAnswersResponse;
import com.fluffy.exam.domain.ExamStatus;
import com.fluffy.exam.domain.dto.AuthorDto;
import com.fluffy.exam.domain.dto.ExamSummaryDto;
import com.fluffy.exam.domain.dto.MyExamSummaryDto;
import com.fluffy.exam.domain.dto.SubmittedExamSummaryDto;
import com.fluffy.exam.ui.request.CreateExamWebRequest;
import com.fluffy.exam.ui.request.PublishExamWebRequest;
import com.fluffy.exam.ui.request.UpdateExamDescriptionWebRequest;
import com.fluffy.exam.ui.request.UpdateExamQuestionsWebRequest;
import com.fluffy.exam.ui.request.UpdateExamTitleWebRequest;
import com.fluffy.global.response.PageInfo;
import com.fluffy.global.response.PageResponse;
import com.fluffy.support.AbstractDocumentTest;
import jakarta.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExamDocumentTest extends AbstractDocumentTest {

    @Test
    @DisplayName("출제된 시험 요약 목록을 조회할 수 있다.")
    void getPublishedExamSummaries() throws Exception {
        PageInfo pageInfo = new PageInfo(0, 2, 4, true, false);
        List<ExamSummaryDto> summaries = List.of(
                new ExamSummaryDto(1L, "시험1", "설명1", ExamStatus.PUBLISHED,
                        new AuthorDto(1L, "작성자1", "a@gmail.com"),
                        3L, 2L, LocalDateTime.now(), LocalDateTime.now()),
                new ExamSummaryDto(2L, "시험2", "설명2", ExamStatus.PUBLISHED,
                        new AuthorDto(2L, "작성자2", "b@gmail.com"),
                        3L, 1L, LocalDateTime.now(), LocalDateTime.now())
        );
        PageResponse<ExamSummaryDto> response = new PageResponse<>(pageInfo, summaries);

        when(examQueryService.getPublishedExamSummaries(any()))
                .thenReturn(response);

        when(examQueryService.getPublishedExamSummaries(any()))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/exams")
                        .param("page", "0")
                        .param("size", "2")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(response))
                )
                .andDo(restDocs.document(
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
                ));
    }

    @Test
    @DisplayName("내가 출제한 시험 요약 목록을 조회할 수 있다.")
    void getMyExamSummaries() throws Exception {
        PageInfo pageInfo = new PageInfo(0, 2, 4, true, false);
        List<MyExamSummaryDto> summaries = List.of(
                new MyExamSummaryDto(1L, "시험1", "설명1", ExamStatus.PUBLISHED,
                        new AuthorDto(1L, "작성자1", "a@gmail.com"),
                        3L, LocalDateTime.now(), LocalDateTime.now()),
                new MyExamSummaryDto(2L, "시험2", "설명2", ExamStatus.PUBLISHED,
                        new AuthorDto(1L, "작성자1", "a@gmail.com"),
                        2L, LocalDateTime.now(), LocalDateTime.now())
        );
        PageResponse<MyExamSummaryDto> response = new PageResponse<>(pageInfo, summaries);

        when(examQueryService.getMyExamSummaries(any(), any(), any()))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/exams/mine")
                        .param("page", "0")
                        .param("size", "2")
                        .accept(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("accessToken", "{ACCESS_TOKEN}"))
                )
                .andExpect(status().isOk())
                .andDo(restDocs.document(
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
                ));
    }

    @Test
    @DisplayName("시험 상세 요약 정보를 조회할 수 있다.")
    void getExamDetailSummary() throws Exception {
        ExamDetailSummaryResponse response = new ExamDetailSummaryResponse(
                1L,
                "시험1",
                "설명1",
                ExamStatus.PUBLISHED.name(),
                new AuthorDto(1L, "작성자1", "https://avatar.com"),
                3L,
                2L,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(examQueryService.getExamDetailSummary(any(), any()))
                .thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/exams/{examId}/summary", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("accessToken", "{ACCESS_TOKEN}"))
                )
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(response))
                )
                .andDo(restDocs.document(
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
                ));
    }

    @Test
    @DisplayName("시험 상세 정보를 조회할 수 있다.")
    void getExamDetail() throws Exception {
        ExamDetailResponse response = new ExamDetailResponse(1L, "시험1", "설명1", ExamStatus.PUBLISHED.name(),
                List.of(
                        new AnswerQuestionResponse(1L, "질문1", "지문", "SHORT_ANSWER"),
                        new AnswerQuestionResponse(2L, "질문2", "지문", "LONG_ANSWER"),
                        new ChoiceQuestionResponse(3L, "질문3", "지문", "SINGLE_CHOICE",
                                List.of(new QuestionOptionResponse(1L, "선택1"), new QuestionOptionResponse(2L, "선택2"))
                        ),
                        new ChoiceQuestionResponse(4L, "질문4", "지문", "MULTIPLE_CHOICE",
                                List.of(new QuestionOptionResponse(3L, "선택3"), new QuestionOptionResponse(4L, "선택4"))
                        ),
                        new ChoiceQuestionResponse(5L, "질문5", "지문", "TRUE_OR_FALSE",
                                List.of(new QuestionOptionResponse(5L, "TRUE"), new QuestionOptionResponse(6L, "FALSE"))
                        )

                ), LocalDateTime.now(), LocalDateTime.now());

        when(examQueryService.getExamDetail(any()))
                .thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/exams/{examId}", 1)
                        .param("page", "0")
                        .param("size", "2")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(response))
                )
                .andDo(restDocs.document(
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
                ));
    }

    @Test
    @DisplayName("시험 상세 정보와 답안을 함께 조회할 수 있다.")
    void getExamDetailWithAnswers() throws Exception {
        ExamWithAnswersResponse response = new ExamWithAnswersResponse(1L, "시험1", "설명1", ExamStatus.PUBLISHED.name(),
                List.of(
                        new ExamWithAnswersResponse.AnswerQuestionWithAnswersResponse(1L, "질문1", "지문", "SHORT_ANSWER",
                                "정답1"),
                        new ExamWithAnswersResponse.AnswerQuestionWithAnswersResponse(2L, "질문2", "지문", "LONG_ANSWER",
                                "정답2"),
                        new ExamWithAnswersResponse.ChoiceQuestionWithAnswersResponse(3L, "질문3", "지문", "SINGLE_CHOICE",
                                List.of(new ExamWithAnswersResponse.ChoiceQuestionWithAnswersResponse.QuestionOptionWithAnswersResponse(
                                                1L, "선택1", true),
                                        new ExamWithAnswersResponse.ChoiceQuestionWithAnswersResponse.QuestionOptionWithAnswersResponse(
                                                2L, "선택2", false))
                        ),
                        new ExamWithAnswersResponse.ChoiceQuestionWithAnswersResponse(4L, "질문4", "지문",
                                "MULTIPLE_CHOICE",
                                List.of(new ExamWithAnswersResponse.ChoiceQuestionWithAnswersResponse.QuestionOptionWithAnswersResponse(
                                                3L, "선택1", true),
                                        new ExamWithAnswersResponse.ChoiceQuestionWithAnswersResponse.QuestionOptionWithAnswersResponse(
                                                4L, "선택2", true))
                        ),
                        new ExamWithAnswersResponse.ChoiceQuestionWithAnswersResponse(5L, "질문5", "지문", "TRUE_OR_FALSE",
                                List.of(new ExamWithAnswersResponse.ChoiceQuestionWithAnswersResponse.QuestionOptionWithAnswersResponse(
                                                5L, "TRUE", true),
                                        new ExamWithAnswersResponse.ChoiceQuestionWithAnswersResponse.QuestionOptionWithAnswersResponse(
                                                6L, "FALSE", false))
                        )
                ), LocalDateTime.now(), LocalDateTime.now());

        when(examQueryService.getExamWithAnswers(any(), any()))
                .thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/exams/{examId}/with-answers", 1)
                        .param("page", "0")
                        .param("size", "2")
                        .accept(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("accessToken", "{ACCESS_TOKEN}"))
                )
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(response))
                )
                .andDo(restDocs.document(
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
                ));
    }

    @Test
    @DisplayName("내가 제출한 시험 요약 목록을 조회할 수 있다.")
    void getSubmittedExamSummaries() throws Exception {
        PageInfo pageInfo = new PageInfo(0, 1, 1, false, false);
        List<SubmittedExamSummaryDto> summaries = List.of(new SubmittedExamSummaryDto(
                1L,
                "시험1",
                "설명1",
                new AuthorDto(1L, "작성자1", "https://avatar.com"),
                3L,
                LocalDateTime.now()
        ));
        PageResponse<SubmittedExamSummaryDto> response = new PageResponse<>(pageInfo, summaries);

        when(examQueryService.getSubmittedExamSummaries(any(), any()))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/exams/submitted")
                        .param("page", "0")
                        .param("size", "9")
                        .cookie(new Cookie("accessToken", "{ACCESS_TOKEN}"))
                )
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(response))
                )
                .andDo(restDocs.document(
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
                ));
    }

    @Test
    @DisplayName("시험을 생성할 수 있다.")
    void createExam() throws Exception {
        CreateExamWebRequest request = new CreateExamWebRequest("시험 제목");

        CreateExamResponse response = new CreateExamResponse(1L, "시험 제목");

        when(examService.create(any()))
                .thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/exams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("accessToken", "{ACCESS_TOKEN}"))
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isCreated(),
                        header().string("Location", "/api/v1/exams/1"),
                        content().json(objectMapper.writeValueAsString(response))
                )
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("title").description("시험 제목")
                        ),
                        responseFields(
                                fieldWithPath("id").description("시험 ID"),
                                fieldWithPath("title").description("시험 제목")
                        )
                ));
    }

    @Test
    @DisplayName("시험을 출제할 수 있다.")
    void publish() throws Exception {
        PublishExamWebRequest request = new PublishExamWebRequest(
                List.of(
                        new ShortAnswerQuestionRequest("질문1", "지문", "SHORT_ANSWER", "답"),
                        new LongAnswerQuestionRequest("질문2", "지문", "LONG_ANSWER"),
                        new SingleChoiceQuestionRequest("질문3", "지문", "SINGLE_CHOICE",
                                List.of(new QuestionOptionRequest("선택1", false),
                                        new QuestionOptionRequest("선택2", true))),
                        new MultipleChoiceRequest("질문4", "지문", "MULTIPLE_CHOICE",
                                List.of(new QuestionOptionRequest("선택1", true),
                                        new QuestionOptionRequest("선택2", true))),
                        new TrueOrFalseQuestionRequest("질문5", "지문", "TRUE_OR_FALSE", true)
                )
        );

        doNothing().when(examService).publish(any());

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/exams/{examId}/publish", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("accessToken", "{ACCESS_TOKEN}"))
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk()
                )
                .andDo(restDocs.document(
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
                ));
    }

    @Test
    @DisplayName("시험 이미지에 대한 Presigend URL을 생성할 수 있다.")
    void createPresignedUrl() throws Exception {
        ExamImagePresignedUrlRequest request = new ExamImagePresignedUrlRequest("BLA-BLA.png", 1024L);
        ExamImagePresignedUrlResponse response = new ExamImagePresignedUrlResponse(
                "https://cdn.fluffy.run/exams/1/QWER-1234.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=...",
                "https://cdn.fluffy.run/exams/1/QWER-1234.png"
        );

        when(examImageService.createPresignedUrl(any(), any(), any()))
                .thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/exams/{examId}/images/presigned-url", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("accessToken", "{ACCESS_TOKEN}"))
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(response))
                )
                .andDo(restDocs.document(
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
                ));
    }

    @Test
    @DisplayName("시험 문제를 수정할 수 있다.")
    void updateQuestions() throws Exception {
        UpdateExamQuestionsWebRequest request = new UpdateExamQuestionsWebRequest(
                List.of(
                        new ShortAnswerQuestionRequest("질문1", "지문", "SHORT_ANSWER", "답"),
                        new LongAnswerQuestionRequest("질문2", "지문", "LONG_ANSWER"),
                        new SingleChoiceQuestionRequest("질문3", "지문", "SINGLE_CHOICE",
                                List.of(new QuestionOptionRequest("선택1", false),
                                        new QuestionOptionRequest("선택2", true))),
                        new MultipleChoiceRequest("질문4", "지문", "MULTIPLE_CHOICE",
                                List.of(new QuestionOptionRequest("선택1", true),
                                        new QuestionOptionRequest("선택2", true))),
                        new TrueOrFalseQuestionRequest("질문5", "지문", "TRUE_OR_FALSE", true)
                )
        );
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/v1/exams/{examId}/questions", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("accessToken", "{ACCESS_TOKEN}"))
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isNoContent()
                )
                .andDo(restDocs.document(
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
                ));
    }

    @Test
    @DisplayName("시험 제목을 수정할 수 있다.")
    void updateTitle() throws Exception {
        UpdateExamTitleWebRequest request = new UpdateExamTitleWebRequest("시험 제목");

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/exams/{examId}/title", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("accessToken", "{ACCESS_TOKEN}"))
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("examId").description("시험 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("시험 제목")
                        )
                ));
    }

    @Test
    @DisplayName("시험 설명을 수정할 수 있다.")
    void updateDescription() throws Exception {
        UpdateExamDescriptionWebRequest request = new UpdateExamDescriptionWebRequest("시험 설명");

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/exams/{examId}/description", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("accessToken", "{ACCESS_TOKEN}"))
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("examId").description("시험 ID")
                        ),
                        requestFields(
                                fieldWithPath("description").description("시험 설명")
                        )
                ));
    }
}
