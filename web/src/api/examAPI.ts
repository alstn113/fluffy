import { apiV1Client } from './apiClient';
import { QuestionBaseRequest, QuestionResponse, QuestionWithAnswersResponse } from './questionAPI';
import { PageParmas } from './request';
import { PageResponse } from './response';

export const ExamAPI = {
  getPublishedExamSummaries: async ({ page, size }: PageParmas) => {
    const { data } = await apiV1Client.get<PageResponse<ExamSummaryResponse>>('/exams', {
      params: { page, size },
    });
    return data;
  },

  getMyExamSummaries: async ({ page, size, status }: MyExamSummaryParams) => {
    const { data } = await apiV1Client.get<PageResponse<ExamSummaryResponse>>('/exams/mine', {
      params: { status, page, size },
    });
    return data;
  },

  getById: async (id: number) => {
    const { data } = await apiV1Client.get<ExamResponse>(`/exams/${id}`);
    return data;
  },

  getWithAnswersById: async (id: number) => {
    const { data } = await apiV1Client.get<ExamWithAnswersResponse>(`/exams/${id}/with-answers`);
    return data;
  },

  getSubmittedExamSummaries: async ({ page, size }: PageParmas) => {
    const { data } = await apiV1Client.get<PageResponse<SubmittedExamSummaryResponse>>(
      '/exams/submitted',
      {
        params: { page, size },
      },
    );
    return data;
  },

  create: async (request: CreateExamRequest) => {
    const { data } = await apiV1Client.post<CreateExamResponse>('/exams', request);
    return data;
  },

  publish: async ({ examId, request }: PublishExamParams) => {
    const { data } = await apiV1Client.post<void>(`/exams/${examId}/publish`, request);
    return data;
  },

  updateQuestions: async ({ examId, request }: UpdateExamQuestionsParams) => {
    const { data } = await apiV1Client.put<void>(`/exams/${examId}/questions`, request);
    return data;
  },

  updateTitle: async ({ examId, request }: UpdateExamTitleParams) => {
    const { data } = await apiV1Client.patch<void>(`/exams/${examId}/title`, request);
    return data;
  },

  updateDescription: async ({ examId, request }: UpdateExamDescriptionParams) => {
    const { data } = await apiV1Client.patch<void>(`/exams/${examId}/description`, request);
    return data;
  },
};

export const EXAM_STATUS = {
  draft: 'DRAFT',
  published: 'PUBLISHED',
} as const;
export type ExamStatusType = (typeof EXAM_STATUS)[keyof typeof EXAM_STATUS];

export interface ExamSummaryParams {
  page: number;
  size: number;
}

export interface ExamSummaryResponse {
  id: number;
  title: string;
  description: string;
  status: typeof EXAM_STATUS.draft;
  author: AuthorResponse;
  questionCount: number;
  createdAt: string;
  updatedAt: string;
}

export interface MyExamSummaryParams extends PageParmas {
  status: ExamStatusType;
}

interface AuthorResponse {
  id: number;
  name: string;
  avatarUrl: string;
}

interface ExamResponse {
  id: number;
  title: string;
  description: string;
  status: ExamStatusType;
  questions: QuestionResponse[];
  createdAt: string;
  updatedAt: string;
}

interface ExamWithAnswersResponse extends ExamResponse {
  questions: QuestionWithAnswersResponse[];
}

export interface SubmittedExamSummaryResponse {
  examId: number;
  title: string;
  description: string;
  author: AuthorResponse;
  submissionCount: number;
  lastSubmissionDate: string;
}

interface CreateExamRequest {
  title: string;
}

interface CreateExamResponse {
  id: number;
  title: string;
}

interface PublishExamParams {
  examId: number;
  request: PublishExamRequest;
}

interface PublishExamRequest {
  questions: QuestionBaseRequest[];
  startAt: string | null;
  endAt: string | null;
}

interface UpdateExamQuestionsParams {
  examId: number;
  request: UpdateExamQuestionsRequest;
}

export interface UpdateExamQuestionsRequest {
  questions: QuestionBaseRequest[];
}

interface UpdateExamTitleParams {
  examId: number;
  request: { title: string };
}

interface UpdateExamDescriptionParams {
  examId: number;
  request: { description: string };
}
