import { useSuspenseQuery } from '@tanstack/react-query';
import { UseQueryOptionsOf } from '../../types';
import { ExamCommentAPI } from '@/api/examCommentAPI';

const useGetExamRootComments = (
  examId: number,
  options: UseQueryOptionsOf<typeof ExamCommentAPI.getRootComments> = {},
) => {
  return useSuspenseQuery({
    ...options,
    queryKey: getKey(examId),
    queryFn: fetcher(examId),
  });
};

const getKey = (examId: number) => ['exams', examId, 'comments'];
const fetcher = (examId: number) => async () => await ExamCommentAPI.getRootComments(examId);

useGetExamRootComments.getKey = getKey;
useGetExamRootComments.fetcher = fetcher;

export default useGetExamRootComments;
