import { useMutation } from '@tanstack/react-query';
import { UseMutationOptionsOf } from '../types';
import { ExamAPI } from '@/api/examAPI';
import toast from 'react-hot-toast';

const useUpdateExamQuestions = (
  options: UseMutationOptionsOf<typeof ExamAPI.updateQuestions> = {},
) => {
  return useMutation({
    onError: (error) => {
      const message =
        error.response?.data?.detail || '시험 문제를 업데이트하는 중에 오류가 발생했습니다.';
      toast.error(message);
    },
    onSuccess: () => {
      toast.success('시험 문제가 업데이트되었습니다.');
    },
    mutationFn: ExamAPI.updateQuestions,
    ...options,
  });
};

export default useUpdateExamQuestions;
