import axios from 'axios';
import { API_V1_URL } from '~/constants';

export const apiV1Client = axios.create({
  baseURL: API_V1_URL,
});
