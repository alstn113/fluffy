import { useSuspenseQuery } from '@tanstack/react-query';
import { UseQueryOptionsOf } from '../types';
import { ExamAPI } from '@/api/examAPI.ts';

const useGetMyExamSummaries = (
  options: UseQueryOptionsOf<typeof ExamAPI.getMyExamSummaries> = {},
) => {
  return useSuspenseQuery({
    ...options,
    queryKey: getKey(),
    queryFn: fetcher(),
  });
};

const getKey = () => ['my-exams'];
const fetcher = () => async () => await ExamAPI.getSummaries();

useGetMyExamSummaries.getKey = getKey;
useGetMyExamSummaries.fetcher = fetcher;

export default useGetMyExamSummaries;
