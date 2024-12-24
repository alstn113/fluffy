import { useSuspenseQuery } from '@tanstack/react-query';
import { UseQueryOptionsOf } from '../types';
import { ExamAPI } from '@/api/examAPI.ts';

const useGetExamSummaries = (
  page: number = 0,
  size: number = 10,
  options: UseQueryOptionsOf<typeof ExamAPI.getPublishedExamSummaries> = {},
) => {
  return useSuspenseQuery({
    ...options,
    queryKey: getKey(page, size),
    queryFn: fetcher(page, size),
  });
};

const getKey = (page: number, size: number) => ['examSummaries', { page, size }];
const fetcher = (page: number, size: number) => () =>
  ExamAPI.getPublishedExamSummaries({ page, size });

useGetExamSummaries.getKey = getKey;
useGetExamSummaries.fetcher = fetcher;

export default useGetExamSummaries;
