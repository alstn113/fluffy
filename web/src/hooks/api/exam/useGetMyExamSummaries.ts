import { useSuspenseQuery } from '@tanstack/react-query';
import { UseQueryOptionsOf } from '../types';
import { ExamAPI, ExamStatusType } from '@/api/examAPI.ts';

const useGetMyExamSummaries = (
  status: ExamStatusType,
  page: number = 0,
  size: number = 12,
  options: UseQueryOptionsOf<typeof ExamAPI.getMyExamSummaries> = {},
) => {
  return useSuspenseQuery({
    ...options,
    queryKey: getKey(status, page, size),
    queryFn: fetcher(status, page, size),
  });
};

const getKey = (status: ExamStatusType, page: number, size: number) => [
  'myExamSummaries',
  { status, page, size },
];
const fetcher = (status: ExamStatusType, page: number, size: number) => () =>
  ExamAPI.getMyExamSummaries({ status, page, size });

useGetMyExamSummaries.getKey = getKey;
useGetMyExamSummaries.fetcher = fetcher;

export default useGetMyExamSummaries;
