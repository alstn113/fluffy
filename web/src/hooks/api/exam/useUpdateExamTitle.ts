import { useMutation } from '@tanstack/react-query';
import { UseMutationOptionsOf } from '../types';
import { ExamAPI } from '@/api/examAPI';
import toast from 'react-hot-toast';

const useUpdateExamTitle = (options: UseMutationOptionsOf<typeof ExamAPI.updateTitle> = {}) => {
  return useMutation({
    onError: (error) => {
      const status = error.response?.status;
      const message =
        error.response?.data?.detail || '시험 제목을 업데이트하는 중에 문제가 발생했습니다.';
      toast.error(`${status}: ${message}`);
    },
    onSuccess: () => {
      toast.success('시험 제목이 업데이트되었습니다.');
    },
    mutationFn: ExamAPI.updateTitle,
    ...options,
  });
};

export default useUpdateExamTitle;
