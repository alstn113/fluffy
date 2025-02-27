import { apiV1Client } from './apiClient';

export const ExamCommentAPI = {
  getRootComments: async (examId: number) => {
    const { data } = await apiV1Client.get<GetExamRootCommentsResponse>(
      `/exams/${examId}/comments`,
    );
    return data;
  },

  getReplyComments: async (rootCommentId: number) => {
    const { data } = await apiV1Client.get<GetExamReplyCommentsResponse>(
      `/exams/comments/${rootCommentId}/replies`,
    );
    return data;
  },

  createComment: async ({ examId, request }: CreateExamCommentParams) => {
    const { data } = await apiV1Client.post<CreateExamCommentResponse>(
      `/exams/${examId}/comments`,
      request,
    );
    return data;
  },

  deleteComment: async (commentId: number) => {
    const { data } = await apiV1Client.delete<DeleteCommentResponse>(
      `/exams/comments/${commentId}`,
    );
    return data;
  },
};

interface GetExamRootCommentsResponse {
  comments: ExamRootCommentResponse[];
}

interface ExamRootCommentResponse {
  id: number;
  content: string;
  author: AuthorResponse;
  replyCount: number;
  isDeleted: boolean;
  createdAt: string;
  updatedAt: string;
}

interface GetExamReplyCommentsResponse {
  replies: ExamReplyCommentResponse[];
}

interface ExamReplyCommentResponse {
  id: number;
  content: string;
  author: AuthorResponse;
  createdAt: string;
  updatedAt: string;
}

interface CreateExamCommentRequest {
  content: string;
  parentCommentId: number | null;
}

interface CreateExamCommentParams {
  examId: number;
  request: CreateExamCommentRequest;
}

interface CreateExamCommentResponse {
  id: number;
  content: string;
  examId: number;
  parentCommentId: number | null;
  author: AuthorResponse;
  createdAt: string;
}

interface AuthorResponse {
  id: number;
  name: string;
  avatarUrl: string;
}

interface DeleteCommentResponse {
  id: number;
}
