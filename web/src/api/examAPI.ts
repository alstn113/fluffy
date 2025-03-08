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
    const { data } = await apiV1Client.get<PageResponse<MyExamSummaryResponse>>('/exams/mine', {
      params: { status, page, size },
    });
    return data;
  },

  getExamDetailSummary: async (id: number) => {
    const { data } = await apiV1Client.get<ExamDetailSummaryResponse>(`/exams/${id}/summary`);
    return data;
  },

  getExamDetail: async (id: number) => {
    const { data } = await apiV1Client.get<ExamDetailResponse>(`/exams/${id}`);
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

  getPresignedUrl: async ({ examId, fileSize, imageName }: ExamImagePresigendUrlRequest) => {
    const { data } = await apiV1Client.post<ExamImagePresignedUrlResponse>(
      `/exams/${examId}/images/presigned-url`,
      {
        imageName,
        fileSize,
      },
    );

    return data;
  },

  uploadImage: async ({ examId, image }: UploadImageRequest) => {
    const { presignedUrl, imageUrl } = await ExamAPI.getPresignedUrl({
      examId,
      imageName: image.name,
      fileSize: image.size,
    });

    await apiV1Client.put<void>(presignedUrl, image, {
      headers: {
        'Content-Type': image.type,
      },
      withCredentials: false,
    });

    return { imageUrl };
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

  like: async (examId: number) => {
    const { data } = await apiV1Client.post<void>(`/exams/${examId}/like`);

    return data;
  },

  unlike: async (examId: number) => {
    const { data } = await apiV1Client.delete<void>(`/exams/${examId}/like`);
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
  likeCount: number;
  createdAt: string;
  updatedAt: string;
}

export interface MyExamSummaryParams extends PageParmas {
  status: ExamStatusType;
}

export interface MyExamSummaryResponse {
  id: number;
  title: string;
  description: string;
  status: ExamStatusType;
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

export interface ExamDetailSummaryResponse {
  id: number;
  title: string;
  description: string;
  status: ExamStatusType;
  author: AuthorResponse;
  questionCount: number;
  likeCount: number;
  isLiked: boolean;
  createdAt: string;
  updatedAt: string;
}

interface ExamDetailResponse {
  id: number;
  title: string;
  description: string;
  status: ExamStatusType;
  questions: QuestionResponse[];
  createdAt: string;
  updatedAt: string;
}

interface ExamWithAnswersResponse extends ExamDetailResponse {
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

interface ExamImagePresigendUrlRequest {
  imageName: string;
  fileSize: number;
  examId: number;
}

interface ExamImagePresignedUrlResponse {
  presignedUrl: string;
  imageUrl: string;
}

interface UploadImageRequest {
  examId: number;
  image: File;
}
