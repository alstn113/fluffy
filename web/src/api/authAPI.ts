import { apiV1Client } from './apiClient';

export const AuthAPI = {
  getMyInfo: async () => {
    const { data } = await apiV1Client.get<MyInfoResponse>('/auth/me');
    return data;
  },

  logout: async () => {
    const { data } = await apiV1Client.post<void>('/auth/logout');
    return data;
  },
};

export interface MyInfoResponse {
  id: number;
  email: string | null;
  name: string;
  avatarUrl: string;
}
