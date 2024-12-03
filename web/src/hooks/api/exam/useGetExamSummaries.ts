import { useSuspenseQuery } from '@tanstack/react-query';
import { UseQueryOptionsOf } from '../types';
import { ExamAPI } from '@/api/examAPI.ts';

const useGetExamSummaries = (
  options: UseQueryOptionsOf<typeof ExamAPI.getPublishedExamSummaries> = {},
) => {
  return useSuspenseQuery({
    ...options,
    queryKey: getKey(),
    queryFn: fetcher(),
  });
};

const getKey = () => ['exams'];
const fetcher = () => async () => await ExamAPI.getPublishedExamSummaries();

useGetExamSummaries.getKey = getKey;
useGetExamSummaries.fetcher = fetcher;

export default useGetExamSummaries;
