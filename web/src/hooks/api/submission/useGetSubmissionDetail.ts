import { useSuspenseQuery } from '@tanstack/react-query';
import { UseQueryOptionsOf } from '../types';
import { SubmissionAPI } from '@/api/submissionAPI';

const useGetSubmissionDetail = (
  examId: number,
  submissionId: number,
  options: UseQueryOptionsOf<typeof SubmissionAPI.getDetail> = {},
) => {
  return useSuspenseQuery({
    ...options,
    queryKey: getKey(examId, submissionId),
    queryFn: fetcher(examId, submissionId),
  });
};

const getKey = (examId: number, submissionId: number) => ['exams', examId, 'submissions', submissionId];
const fetcher = (examId: number, submissionId: number) => async () =>
  await SubmissionAPI.getDetail({ examId, submissionId });

useGetSubmissionDetail.getKey = getKey;
useGetSubmissionDetail.fetcher = fetcher;

export default useGetSubmissionDetail;
