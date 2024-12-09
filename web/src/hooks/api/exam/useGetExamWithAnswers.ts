import { useSuspenseQuery } from '@tanstack/react-query';
import { ExamAPI } from '@/api/examAPI';
import { UseQueryOptionsOf } from '../types';

const useGetExamWithAnswers = (examId: number, options: UseQueryOptionsOf<typeof ExamAPI.getWithAnswersById> = {}) => {
  return useSuspenseQuery({
    ...options,
    queryKey: getKey(examId),
    queryFn: fetcher(examId),
  });
};

const getKey = (examId: number) => ['exams', examId, 'with-answers'];
const fetcher = (examId: number) => async () => await ExamAPI.getWithAnswersById(examId);

useGetExamWithAnswers.getKey = getKey;
useGetExamWithAnswers.fetcher = fetcher;

export default useGetExamWithAnswers;
