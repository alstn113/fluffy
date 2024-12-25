import { useMutation } from '@tanstack/react-query';
import { UseMutationOptionsOf } from '../types';
import { ExamAPI } from '@/api/examAPI';
import toast from 'react-hot-toast';

const usePublishExam = (options: UseMutationOptionsOf<typeof ExamAPI.publish> = {}) => {
  return useMutation({
    onError: (error) => {
      const message = error.response?.data?.detail || '시험을 출제하는 중에 오류가 발생했습니다.';
      toast.error(message);
    },
    onSuccess: () => {
      toast.success('시험 출제가 완료되었습니다.');
    },
    mutationFn: ExamAPI.publish,
    ...options,
  });
};

export default usePublishExam;
