package com.fluffy.exam.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
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

import com.fluffy.exam.api.request.CreateExamWebRequest;
import com.fluffy.exam.api.request.PublishExamWebRequest;
import com.fluffy.exam.api.request.UpdateExamDescriptionWebRequest;
import com.fluffy.exam.api.request.UpdateExamQuestionsWebRequest;
import com.fluffy.exam.api.request.UpdateExamTitleWebRequest;
import com.fluffy.exam.application.ExamQueryService;
import com.fluffy.exam.application.ExamService;
import com.fluffy.exam.application.request.question.LongAnswerQuestionAppRequest;
import com.fluffy.exam.application.request.question.MultipleChoiceAppRequest;
import com.fluffy.exam.application.request.question.QuestionOptionRequest;
import com.fluffy.exam.application.request.question.ShortAnswerQuestionAppRequest;
import com.fluffy.exam.application.request.question.SingleChoiceQuestionAppRequest;
import com.fluffy.exam.application.request.question.TrueOrFalseQuestionAppRequest;
import com.fluffy.exam.application.response.CreateExamResponse;
import com.fluffy.exam.application.response.ExamDetailResponse;
import com.fluffy.exam.application.response.ExamDetailResponse.AnswerQuestionResponse;
import com.fluffy.exam.application.response.ExamDetailResponse.ChoiceQuestionResponse;
import com.fluffy.exam.application.response.ExamDetailResponse.ChoiceQuestionResponse.QuestionOptionResponse;
import com.fluffy.exam.application.response.ExamWithAnswersResponse;
import com.fluffy.exam.domain.ExamStatus;
import com.fluffy.exam.domain.dto.AuthorDto;
import com.fluffy.exam.domain.dto.ExamSummaryDto;
import com.fluffy.global.response.PageInfo;
import com.fluffy.global.response.PageResponse;
import com.fluffy.support.AbstractDocumentTest;
import jakarta.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

@WebMvcTest(ExamController.class)
class ExamDocumentTest extends AbstractDocumentTest {

    @MockBean
    private ExamService examService;

    @MockBean
    private ExamQueryService examQueryService;

    @Test
    @DisplayName("출제된 시험 요약 목록을 조회할 수 있다.")
    void getPublishedExamSummaries() throws Exception {
        PageInfo pageInfo = new PageInfo(0, 2, 4, true, false);
        List<ExamSummaryDto> summaries = List.of(
                new ExamSummaryDto(1L, "시험1", "설명1", ExamStatus.PUBLISHED,
                        new AuthorDto(1L, "작성자1", "a@gmail.com"),
                        3L, LocalDateTime.now(), LocalDateTime.now()),
                new ExamSummaryDto(2L, "시험2", "설명2", ExamStatus.PUBLISHED,
                        new AuthorDto(2L, "작성자2", "b@gmail.com"),
                        3L, LocalDateTime.now(), LocalDateTime.now())
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
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(response))
                )
                .andDo(document(
                        "api/v1/exams/get-published-exam-summaries",
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지당 항목 수")
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
    @DisplayName("내가 출제한 시험 요약 목록을 조회할 수 있다.")
    void getMyExamSummaries() throws Exception {
        PageInfo pageInfo = new PageInfo(0, 2, 4, true, false);
        List<ExamSummaryDto> summaries = List.of(
                new ExamSummaryDto(1L, "시험1", "설명1", ExamStatus.PUBLISHED,
                        new AuthorDto(1L, "작성자1", "a@gmail.com"),
                        3L, LocalDateTime.now(), LocalDateTime.now()),
                new ExamSummaryDto(2L, "시험2", "설명2", ExamStatus.PUBLISHED,
                        new AuthorDto(1L, "작성자1", "a@gmail.com"),
                        2L, LocalDateTime.now(), LocalDateTime.now())
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
                        .cookie(new Cookie("accessToken", "{ACCESS_TOKEN}"))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document(
                        "api/v1/exams/get-published-exam-summaries",
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지당 항목 수")
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
    @DisplayName("시험 상세 정보를 조회할 수 있다.")
    void getExamDetail() throws Exception {
        ExamDetailResponse response = new ExamDetailResponse(1L, "시험1", "설명1", ExamStatus.PUBLISHED.name(),
                List.of(
                        new AnswerQuestionResponse(1L, "질문1", "SHORT_ANSWER"),
                        new AnswerQuestionResponse(2L, "질문2", "LONG_ANSWER"),
                        new ChoiceQuestionResponse(3L, "질문3", "SINGLE_CHOICE",
                                List.of(new QuestionOptionResponse(1L, "선택1"), new QuestionOptionResponse(2L, "선택2"))
                        ),
                        new ChoiceQuestionResponse(4L, "질문4", "MULTIPLE_CHOICE",
                                List.of(new QuestionOptionResponse(3L, "선택3"), new QuestionOptionResponse(4L, "선택4"))
                        ),
                        new ChoiceQuestionResponse(5L, "질문5", "TRUE_OR_FALSE",
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
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(response))
                )
                .andDo(document(
                        "api/v1/exams/get-exam-detail",
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
                        new ExamWithAnswersResponse.AnswerQuestionWithAnswersResponse(1L, "질문1", "SHORT_ANSWER", "정답1"),
                        new ExamWithAnswersResponse.AnswerQuestionWithAnswersResponse(2L, "질문2", "LONG_ANSWER", "정답2"),
                        new ExamWithAnswersResponse.ChoiceQuestionWithAnswersResponse(3L, "질문3", "SINGLE_CHOICE",
                                List.of(new ExamWithAnswersResponse.ChoiceQuestionWithAnswersResponse.QuestionOptionWithAnswersResponse(
                                                1L, "선택1", true),
                                        new ExamWithAnswersResponse.ChoiceQuestionWithAnswersResponse.QuestionOptionWithAnswersResponse(
                                                2L, "선택2", false))
                        ),
                        new ExamWithAnswersResponse.ChoiceQuestionWithAnswersResponse(4L, "질문4", "MULTIPLE_CHOICE",
                                List.of(new ExamWithAnswersResponse.ChoiceQuestionWithAnswersResponse.QuestionOptionWithAnswersResponse(
                                                3L, "선택1", true),
                                        new ExamWithAnswersResponse.ChoiceQuestionWithAnswersResponse.QuestionOptionWithAnswersResponse(
                                                4L, "선택2", true))
                        ),
                        new ExamWithAnswersResponse.ChoiceQuestionWithAnswersResponse(5L, "질문5", "TRUE_OR_FALSE",
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
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(response))
                )
                .andDo(document(
                        "api/v1/exams/get-exam-detail-with-answers",
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
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        header().string("Location", "/api/v1/exams/1"),
                        content().json(objectMapper.writeValueAsString(response))
                )
                .andDo(document(
                        "api/v1/exams/create-exam",
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
                        new ShortAnswerQuestionAppRequest("질문1", "SHORT_ANSWER", "답"),
                        new LongAnswerQuestionAppRequest("질문2", "LONG_ANSWER"),
                        new SingleChoiceQuestionAppRequest("질문3", "SINGLE_CHOICE",
                                List.of(new QuestionOptionRequest("선택1", false),
                                        new QuestionOptionRequest("선택2", true))),
                        new MultipleChoiceAppRequest("질문4", "MULTIPLE_CHOICE",
                                List.of(new QuestionOptionRequest("선택1", true),
                                        new QuestionOptionRequest("선택2", true))),
                        new TrueOrFalseQuestionAppRequest("질문5", "TRUE_OR_FALSE", true)
                ),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
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
                .andDo(document(
                        "api/v1/exams/publish",
                        pathParameters(
                                parameterWithName("examId").description("시험 ID")
                        ),
                        requestFields(
                                fieldWithPath("questions").description("문제 목록"),
                                fieldWithPath("questions[].text").description("문제 내용"),
                                fieldWithPath("questions[].type").description("문제 유형"),
                                fieldWithPath("questions[].correctAnswer").description("정답").optional(),
                                fieldWithPath("questions[].trueOrFalse").description("true or false 정답").optional(),
                                fieldWithPath("questions[].options").description("선택지 목록").optional(),
                                fieldWithPath("questions[].options[].text").description("선택지 내용").optional(),
                                fieldWithPath("questions[].options[].isCorrect").description("정답 여부").optional(),
                                fieldWithPath("startAt").description("시작 시간").optional(),
                                fieldWithPath("endAt").description("종료 시간").optional()
                        )
                ));
    }

    @Test
    @DisplayName("시험 문제를 수정할 수 있다.")
    void updateQuestions() throws Exception {
        UpdateExamQuestionsWebRequest request = new UpdateExamQuestionsWebRequest(
                List.of(
                        new ShortAnswerQuestionAppRequest("질문1", "SHORT_ANSWER", "답"),
                        new LongAnswerQuestionAppRequest("질문2", "LONG_ANSWER"),
                        new SingleChoiceQuestionAppRequest("질문3", "SINGLE_CHOICE",
                                List.of(new QuestionOptionRequest("선택1", false),
                                        new QuestionOptionRequest("선택2", true))),
                        new MultipleChoiceAppRequest("질문4", "MULTIPLE_CHOICE",
                                List.of(new QuestionOptionRequest("선택1", true),
                                        new QuestionOptionRequest("선택2", true))),
                        new TrueOrFalseQuestionAppRequest("질문5", "TRUE_OR_FALSE", true)
                )
        );
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/v1/exams/{examId}/questions", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("accessToken", "{ACCESS_TOKEN}"))
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpectAll(
                        status().isNoContent()
                )
                .andDo(document(
                        "api/v1/exams/questions",
                        pathParameters(
                                parameterWithName("examId").description("시험 ID")
                        ),
                        requestFields(
                                fieldWithPath("questions").description("문제 목록"),
                                fieldWithPath("questions[].text").description("문제 내용"),
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
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document(
                        "api/v1/exams/update-title",
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
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document(
                        "api/v1/exams/update-description",
                        pathParameters(
                                parameterWithName("examId").description("시험 ID")
                        ),
                        requestFields(
                                fieldWithPath("description").description("시험 설명")
                        )
                ));
    }
}
