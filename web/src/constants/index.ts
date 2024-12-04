export const API_V1_URL =
  (import.meta.env.VITE_API_V1_URL as string) || 'http://localhost:3000/api/v1';

export const GITHUB_OAUTH_LOGIN_URL = `${API_V1_URL}/auth/oauth2/redirect/github`;

export const Routes = {
  home: () => '/',
  about: () => '/about',
  exam: {
    detail: (examId: number) => `/exams/${examId}`,
    management: {
      overview: (examId: number) => `/exams/${examId}/management/overview`,
      questions: (examId: number) => `/exams/${examId}/management/questions`,
      analytics: (examId: number) => `/exams/${examId}/management/analytics`,
      settings: (examId: number) => `/exams/${examId}/management/settings`,
    },
  },
  dashboard: () => '/dashboard',
} as const;
