import { apiV1Client } from './apiClient';

export const AuthAPI = {
  signup: async (request: SignupRequest) => {
    const { data } = await apiV1Client.post<void>('/auth/signup', request);
    return data;
  },

  login: async (request: loginRequest) => {
    const { data } = await apiV1Client.post<void>('/auth/login', request);
    return data;
  },

  logout: async () => {
    const { data } = await apiV1Client.post<void>('/auth/logout');
    return data;
  },
};

interface SignupRequest {
  username: string;
  password: string;
}

interface loginRequest {
  username: string;
  password: string;
}
