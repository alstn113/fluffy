export const GITHUB_OAUTH_LOGIN_URL = `/api/v1/auth/oauth2/redirect/github`;
export const GOOGLE_OAUTH_LOGIN_URL = `/api/v1/auth/oauth2/redirect/google`;

export const Routes = {
  home: () => '/',
  about: () => '/about',
  exam: {
    intro: (examId: number) => `/exams/${examId}/intro`,
    progress: (examId: number) => `/exams/${examId}/progress`,
    submissions: (examId: number) => `/exams/${examId}/submissions`,
    management: {
      overview: (examId: number) => `/exams/${examId}/management/overview`,
      questions: (examId: number) => `/exams/${examId}/management/questions`,
      analytics: (examId: number) => `/exams/${examId}/management/analytics`,
      analytics_detail: (examId: number, submissionId: number) =>
        `/exams/${examId}/management/analytics?submissionId=${submissionId}`,
      settings: (examId: number) => `/exams/${examId}/management/settings`,
    },
  },
  dashboard: () => '/dashboard',
} as const;
