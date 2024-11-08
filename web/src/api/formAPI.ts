import { apiV1Client } from './apiClient';
import { QuestionBaseRequest, QuestionResponse } from './questionAPI';

export const FormAPI = {
  create: async (request: CreateFormRequest) => {
    const { data } = await apiV1Client.post<CreateFormResponse>('/forms', request);
    return data;
  },

  getAll: async () => {
    const { data } = await apiV1Client.get<FormResponse[]>('/forms');
    return data;
  },

  getById: async (id: string) => {
    const { data } = await apiV1Client.get<FormResponse>(`/forms/${id}`);
    return data;
  },

  draft: async ({ formId, request }: DraftFormParams) => {
    const { data } = await apiV1Client.put<FormResponse>(`/forms/${formId}/draft`, request);
    return data;
  },

  publish: async ({ formId, request }: PublishFormParams) => {
    const { data } = await apiV1Client.put<FormResponse>(
      `/api/v1/forms/${formId}/publish`,
      request,
    );
    return data;
  },
};

interface CreateFormRequest {
  title: string;
}

interface CreateFormResponse {
  id: string;
  title: string;
  status: typeof FORM_STATUS.draft;
}

export const FORM_STATUS = {
  draft: 'DRAFT',
  published: 'PUBLISHED',
} as const;
export type FormStatusType = (typeof FORM_STATUS)[keyof typeof FORM_STATUS];

interface FormResponse {
  id: string;
  title: string;
  description: string;
  questions: QuestionResponse[];
  createdAt: string;
  updatedAt: string;
}

interface DraftFormParams {
  formId: string;
  request: DraftFormRequest;
}

interface DraftFormRequest {
  todo: string;
}

interface PublishFormParams {
  formId: string;
  request: PublishFormRequest;
}

export interface PublishFormRequest {
  questions: QuestionBaseRequest[];
}
