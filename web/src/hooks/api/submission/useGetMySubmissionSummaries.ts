import { useSuspenseQuery } from '@tanstack/react-query';
import { UseQueryOptionsOf } from '../types';
import { SubmissionAPI } from '@/api/submissionAPI';

const useGetMySubmissionSummaries = (
  examId: number,
  options: UseQueryOptionsOf<typeof SubmissionAPI.getMySubmissionSummaries> = {},
) => {
  return useSuspenseQuery({
    ...options,
    queryKey: getKey(examId),
    queryFn: fetcher(examId),
  });
};

const getKey = (examId: number) => ['exams', examId, 'my-submissions'];
const fetcher = (examId: number) => async () =>
  await SubmissionAPI.getMySubmissionSummaries(examId);

useGetMySubmissionSummaries.getKey = getKey;
useGetMySubmissionSummaries.fetcher = fetcher;

export default useGetMySubmissionSummaries;
