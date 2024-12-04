import { apiV1Client } from './apiClient';
import { ChoiceQuestionType, QuestionType, TextQuestionType } from './questionAPI';

export const SubmissionAPI = {
  getSummaries: async (examId: number) => {
    const { data } = await apiV1Client.get<SubmissionSummaryResponse[]>(
      `/exams/${examId}/submissions`,
    );
    return data;
  },

  getDetail: async ({ examId, submissionId }: SubmissionDetailParams) => {
    const { data } = await apiV1Client.get<SubmissionDetailResponse>(
      `/exams/${examId}/submissions/${submissionId}`,
    );
    return data;
  },

  submit: async ({ examId, request }: SubmissionParams) => {
    const { data } = await apiV1Client.post<void>(`/exams/${examId}/submissions`, request);
    return data;
  },
};

interface ParticipantResponse {
  id: number;
  name: string;
  email: string | null;
  avatarUrl: string;
}

export interface SubmissionSummaryResponse {
  id: number;
  participant: ParticipantResponse;
  submittedAt: string;
}

interface SubmissionDetailParams {
  examId: number;
  submissionId: number;
}

interface SubmissionDetailResponse {
  id: number;
  answers: AnswerBaseResponse[];
  participant: ParticipantResponse;
  submittedAt: string;
}

export interface AnswerBaseResponse {
  id: number;
  questionId: number;
  text: string;
  type: QuestionType;
}

export interface TextAnswerResponse extends AnswerBaseResponse {
  id: number;
  questionId: number;
  text: string;
  type: TextQuestionType;
  answer: string;
  correctAnswer: string;
}

export interface ChoiceAnswerResponse extends AnswerBaseResponse {
  id: number;
  questionId: number;
  text: string;
  type: ChoiceQuestionType;
  choices: ChoiceResponse[];
}

interface ChoiceResponse {
  questionOptionId: number;
  text: string;
  isCorrect: boolean;
  isSelected: boolean;
}

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
