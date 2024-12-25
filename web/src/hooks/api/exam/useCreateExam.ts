import { ExamAPI } from '@/api/examAPI';
import { UseMutationOptionsOf } from '../types';
import { useMutation } from '@tanstack/react-query';
import toast from 'react-hot-toast';

const useCreateExam = (options: UseMutationOptionsOf<typeof ExamAPI.create> = {}) => {
  return useMutation({
    onError: (error) => {
      const message = error.response?.data?.detail || '시험을 생성하는 중에 오류가 발생했습니다.';
      toast.error(message);
    },
    mutationFn: ExamAPI.create,
    ...options,
  });
};

export default useCreateExam;
