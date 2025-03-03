import { useSuspenseQuery } from '@tanstack/react-query';
import { UseQueryOptionsOf } from '../../types';
import { ExamCommentAPI } from '@/api/examCommentAPI';

const useGetExamReplyComments = (
  rootCommentId: number,
  options: UseQueryOptionsOf<typeof ExamCommentAPI.getReplyComments> = {},
) => {
  return useSuspenseQuery({
    ...options,
    queryKey: getKey(rootCommentId),
    queryFn: fetcher(rootCommentId),
  });
};

const getKey = (rootCommentId: number) => ['exams', 'comments', rootCommentId, 'replies'];
const fetcher = (rootCommentId: number) => async () =>
  await ExamCommentAPI.getReplyComments(rootCommentId);

useGetExamReplyComments.getKey = getKey;
useGetExamReplyComments.fetcher = fetcher;

export default useGetExamReplyComments;
