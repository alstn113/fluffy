import { useSuspenseQuery } from '@tanstack/react-query';
import { UseQueryOptionsOf } from '../types';
import { ExamAPI } from '@/api/examAPI.ts';

const useGetExams = (options: UseQueryOptionsOf<typeof ExamAPI.getAll> = {}) => {
  return useSuspenseQuery({
    ...options,
    queryKey: getKey(),
    queryFn: fetcher(),
  });
};

const getKey = () => ['exams'];
const fetcher = () => async () => await ExamAPI.getAll();

useGetExams.getKey = getKey;
useGetExams.fetcher = fetcher;

export default useGetExams;
