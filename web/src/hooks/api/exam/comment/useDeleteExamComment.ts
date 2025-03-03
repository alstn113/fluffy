import { UseMutationOptionsOf } from '../../types';
import { useMutation } from '@tanstack/react-query';
import { toast } from 'sonner';
import { ExamCommentAPI } from '@/api/examCommentAPI';

const useDeleteExamComment = (
  options: UseMutationOptionsOf<typeof ExamCommentAPI.deleteComment> = {},
) => {
  return useMutation({
    onError: (error) => {
      const message = error.response?.data?.detail || '댓글 삭제에 실패했습니다.';
      toast.error(message);
    },
    mutationFn: ExamCommentAPI.deleteComment,
    ...options,
  });
};

export default useDeleteExamComment;
