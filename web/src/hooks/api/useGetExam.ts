import { useSuspenseQuery } from '@tanstack/react-query';
import { ExamAPI } from '~/api/examAPI';
import { UseQueryOptionsOf } from './types';

const useGetExam = (examId: string, options: UseQueryOptionsOf<typeof ExamAPI.getById> = {}) => {
  return useSuspenseQuery({
    ...options,
    queryKey: getKey(examId),
    queryFn: fetcher(examId),
  });
};

const getKey = (examId: string) => ['exams', examId];
const fetcher = (examId: string) => async () => await ExamAPI.getById(examId);

useGetExam.getKey = getKey;
useGetExam.fetcher = fetcher;

export default useGetExam;
