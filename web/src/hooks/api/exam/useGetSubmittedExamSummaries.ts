import { useSuspenseQuery } from '@tanstack/react-query';
import { ExamAPI } from '@/api/examAPI';
import { UseQueryOptionsOf } from '../types';

const useGetSubmittedExamSummaries = (
  page: number = 0,
  size: number = 12,
  options: UseQueryOptionsOf<typeof ExamAPI.getSubmittedExamSummaries> = {},
) => {
  return useSuspenseQuery({
    ...options,
    queryKey: getKey(page, size),
    queryFn: fetcher(page, size),
  });
};

const getKey = (page: number, size: number) => ['exams', 'submitted', { page, size }];
const fetcher = (page: number, size: number) => async () =>
  await ExamAPI.getSubmittedExamSummaries({ page, size });

useGetSubmittedExamSummaries.getKey = getKey;
useGetSubmittedExamSummaries.fetcher = fetcher;

export default useGetSubmittedExamSummaries;
