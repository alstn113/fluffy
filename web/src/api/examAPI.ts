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

  getById: async (id: number) => {
    const { data } = await apiV1Client.get<ExamResponse>(`/exams/${id}`);
    return data;
  },

  updateQuestions: async ({ examId, request }: UpdateExamQuestionsParams) => {
    const { data } = await apiV1Client.put<void>(`/exams/${examId}/questinos`, request);
    return data;
  },
};

interface CreateExamRequest {
  title: string;
}

interface CreateExamResponse {
  id: number;
  title: string;
  status: typeof EXAM_STATUS.draft;
}

export const EXAM_STATUS = {
  draft: 'DRAFT',
  published: 'PUBLISHED',
} as const;
export type ExamStatusType = (typeof EXAM_STATUS)[keyof typeof EXAM_STATUS];

interface ExamResponse {
  id: number;
  title: string;
  description: string;
  questions: QuestionResponse[];
  createdAt: string;
  updatedAt: string;
}

interface UpdateExamQuestionsParams {
  examId: number;
  request: UpdateExamQuestionsRequest;
}

export interface UpdateExamQuestionsRequest {
  questions: QuestionBaseRequest[];
}
