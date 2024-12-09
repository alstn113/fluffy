export const GITHUB_OAUTH_LOGIN_URL = `/api/v1/auth/oauth2/redirect/github`;

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
