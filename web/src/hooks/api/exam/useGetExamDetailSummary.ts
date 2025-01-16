import { useSuspenseQuery } from '@tanstack/react-query';
import { ExamAPI } from '@/api/examAPI';
import { UseQueryOptionsOf } from '../types';

const useGetExamDetailSummary = (
  examId: number,
  options: UseQueryOptionsOf<typeof ExamAPI.getExamDetailSummary> = {},
) => {
  return useSuspenseQuery({
    ...options,
    queryKey: getKey(examId),
    queryFn: fetcher(examId),
  });
};

const getKey = (examId: number) => ['exams', examId, 'summary'];
const fetcher = (examId: number) => async () => await ExamAPI.getExamDetailSummary(examId);

useGetExamDetailSummary.getKey = getKey;
useGetExamDetailSummary.fetcher = fetcher;

export default useGetExamDetailSummary;
