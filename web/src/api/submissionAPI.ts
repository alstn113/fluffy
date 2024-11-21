import { apiV1Client } from './apiClient';

export const SubmissionAPI = {
  submit: async ({ examId, request }: SubmissionParams) => {
    const { data } = await apiV1Client.post<void>(`/exams/${examId}/submissions`, request);
    return data;
  },
};

interface SubmissionParams {
  examId: number;
  request: SubmissionRequest;
}

interface SubmissionRequest {
  questionResponses: QuestionResponse[];
}

export interface QuestionResponse {
  answers: string[];
}
