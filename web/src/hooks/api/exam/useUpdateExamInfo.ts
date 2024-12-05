import { useMutation } from '@tanstack/react-query';
import { UseMutationOptionsOf } from '../types';
import { ExamAPI } from '@/api/examAPI';
import toast from 'react-hot-toast';

const useUpdateExamInfo = (options: UseMutationOptionsOf<typeof ExamAPI.updateInfo> = {}) => {
  return useMutation({
    onError: (error) => {
      const status = error.response?.status;
      const message =
        error.response?.data?.detail || '시험 정보를 업데이트하는 중에 오류가 발생했습니다.';
      toast.error(`${status}: ${message}`);
    },
    onSuccess: () => {
      toast.success('시험 정보가 업데이트되었습니다.');
    },
    mutationFn: ExamAPI.updateInfo,
    ...options,
  });
};

export default useUpdateExamInfo;
