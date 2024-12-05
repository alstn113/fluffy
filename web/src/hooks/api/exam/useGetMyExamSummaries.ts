import { useSuspenseQuery } from '@tanstack/react-query';
import { UseQueryOptionsOf } from '../types';
import { ExamAPI, ExamStatusType } from '@/api/examAPI.ts';

const useGetMyExamSummaries = (
  status: ExamStatusType,
  options: UseQueryOptionsOf<typeof ExamAPI.getMyExamSummaries> = {},
) => {
  return useSuspenseQuery({
    ...options,
    queryKey: getKey(status),
    queryFn: fetcher(status),
  });
};

const getKey = (status: ExamStatusType) => ['my-exam-summaries', status];
const fetcher = (status: ExamStatusType) => () => ExamAPI.getMyExamSummaries(status);

useGetMyExamSummaries.getKey = getKey;
useGetMyExamSummaries.fetcher = fetcher;

export default useGetMyExamSummaries;
