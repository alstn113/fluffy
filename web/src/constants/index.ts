export const API_V1_URL =
  (import.meta.env.VITE_API_V1_URL as string) || 'http://localhost:3000/api/v1';

export const PAGE_LIST = {
  home: '/',
  about: '/about',
  form: {
    list: '/forms',
    edit: '/forms/:id/edit',
  },
  notFound: '*',
} as const;
