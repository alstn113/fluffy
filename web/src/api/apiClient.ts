import axios from 'axios';

export const apiV1Client = axios.create({
  baseURL: '/api/v1',
  withCredentials: true,
});
