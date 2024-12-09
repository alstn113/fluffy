import { useMutation } from '@tanstack/react-query';
import { UseMutationOptionsOf } from '../types';
import { ExamAPI } from '@/api/examAPI';
import toast from 'react-hot-toast';

const useUpdateExamDescription = (options: UseMutationOptionsOf<typeof ExamAPI.updateDescription> = {}) => {
  return useMutation({
    onError: (error) => {
      const status = error.response?.status;
      const message = error.response?.data?.detail || '시험 설명을 업데이트하는 중에 문제가 발생했습니다.';
      toast.error(`${status}: ${message}`);
    },
    onSuccess: () => {
      toast.success('시험 설명이 업데이트되었습니다.');
    },
    mutationFn: ExamAPI.updateDescription,
    ...options,
  });
};

export default useUpdateExamDescription;
