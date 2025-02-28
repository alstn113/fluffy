import { UseMutationOptionsOf } from '../../types';
import { useMutation } from '@tanstack/react-query';
import { toast } from 'sonner';
import { ExamCommentAPI } from '@/api/examCommentAPI';

const useCreateExamComment = (
  options: UseMutationOptionsOf<typeof ExamCommentAPI.createComment> = {},
) => {
  return useMutation({
    onError: (error) => {
      const message = error.response?.data?.detail || '댓글 생성에 실패했습니다.';
      toast.error(message);
    },
    mutationFn: ExamCommentAPI.createComment,
    ...options,
  });
};

export default useCreateExamComment;
