import { useSuspenseQuery } from '@tanstack/react-query';
import { UseQueryOptionsOf } from '../types';
import { SubmissionAPI } from '@/api/submissionAPI';

const useGetSubmissionSummaries = (
  examId: number,
  options: UseQueryOptionsOf<typeof SubmissionAPI.getSummaries> = {},
) => {
  return useSuspenseQuery({
    ...options,
    queryKey: getKey(examId),
    queryFn: fetcher(examId),
  });
};

const getKey = (examId: number) => ['exams', examId, 'submissions'];
const fetcher = (examId: number) => async () => await SubmissionAPI.getSummaries(examId);

useGetSubmissionSummaries.getKey = getKey;
useGetSubmissionSummaries.fetcher = fetcher;

export default useGetSubmissionSummaries;
