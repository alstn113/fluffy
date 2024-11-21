import { useSuspenseQuery } from '@tanstack/react-query';
import { ExamAPI } from '@/api/examAPI';
import { UseQueryOptionsOf } from '../types';

const useGetExam = (examId: number, options: UseQueryOptionsOf<typeof ExamAPI.getById> = {}) => {
  return useSuspenseQuery({
    ...options,
    queryKey: getKey(examId),
    queryFn: fetcher(examId),
  });
};

const getKey = (examId: number) => ['exams', examId];
const fetcher = (examId: number) => async () => await ExamAPI.getById(examId);

useGetExam.getKey = getKey;
useGetExam.fetcher = fetcher;

export default useGetExam;
