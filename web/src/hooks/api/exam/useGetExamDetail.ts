import { useSuspenseQuery } from '@tanstack/react-query';
import { ExamAPI } from '@/api/examAPI';
import { UseQueryOptionsOf } from '../types';

const useGetExamDetail = (
  examId: number,
  options: UseQueryOptionsOf<typeof ExamAPI.getExamDetail> = {},
) => {
  return useSuspenseQuery({
    ...options,
    queryKey: getKey(examId),
    queryFn: fetcher(examId),
  });
};

const getKey = (examId: number) => ['exams', examId];
const fetcher = (examId: number) => async () => await ExamAPI.getExamDetail(examId);

useGetExamDetail.getKey = getKey;
useGetExamDetail.fetcher = fetcher;

export default useGetExamDetail;
