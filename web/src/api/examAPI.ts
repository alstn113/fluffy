import { apiV1Client } from './apiClient';
import { QuestionBaseRequest, QuestionResponse } from './questionAPI';

export const ExamAPI = {
  create: async (request: CreateExamRequest) => {
    const { data } = await apiV1Client.post<CreateExamResponse>('/exams', request);
    return data;
  },

  getAll: async () => {
    const { data } = await apiV1Client.get<ExamResponse[]>('/exams');
    return data;
  },

  getById: async (id: string) => {
    const { data } = await apiV1Client.get<ExamResponse>(`/exams/${id}`);
    return data;
  },

  draft: async ({ examId, request }: DraftExamParams) => {
    const { data } = await apiV1Client.put<ExamResponse>(`/exams/${examId}/draft`, request);
    return data;
  },

  publish: async ({ examId, request }: PublishExamParams) => {
    const { data } = await apiV1Client.put<ExamResponse>(
      `/api/v1/exams/${examId}/publish`,
      request,
    );
    return data;
  },
};

interface CreateExamRequest {
  title: string;
}

interface CreateExamResponse {
  id: string;
  title: string;
  status: typeof EXAM_STATUS.draft;
}

export const EXAM_STATUS = {
  draft: 'DRAFT',
  published: 'PUBLISHED',
} as const;
export type ExamStatusType = (typeof EXAM_STATUS)[keyof typeof EXAM_STATUS];

interface ExamResponse {
  id: string;
  title: string;
  description: string;
  questions: QuestionResponse[];
  createdAt: string;
  updatedAt: string;
}

interface DraftExamParams {
  examId: string;
  request: DraftExamRequest;
}

interface DraftExamRequest {
  todo: string;
}

interface PublishExamParams {
  examId: string;
  request: PublishExamRequest;
}

export interface PublishExamRequest {
  questions: QuestionBaseRequest[];
}
