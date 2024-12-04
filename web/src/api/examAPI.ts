import { apiV1Client } from './apiClient';
import { QuestionBaseRequest, QuestionResponse, QuestionWithAnswersResponse } from './questionAPI';

export const ExamAPI = {
  getPublishedExamSummaries: async () => {
    const { data } = await apiV1Client.get<ExamSummaryResponse[]>('/exams');
    return data;
  },

  getMyExamSummaries: async (status: ExamStatusType) => {
    const { data } = await apiV1Client.get<ExamSummaryResponse[]>('/exams/mine', {
      params: { status },
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
};

export const EXAM_STATUS = {
  draft: 'DRAFT',
  published: 'PUBLISHED',
} as const;
export type ExamStatusType = (typeof EXAM_STATUS)[keyof typeof EXAM_STATUS];

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
  startAt: string;
  endAt: string;
}

interface UpdateExamQuestionsParams {
  examId: number;
  request: UpdateExamQuestionsRequest;
}

export interface UpdateExamQuestionsRequest {
  questions: QuestionBaseRequest[];
}
