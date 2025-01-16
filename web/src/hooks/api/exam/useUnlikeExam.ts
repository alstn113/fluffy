import { UseMutationOptionsOf } from '@/hooks/api/types.ts';
import { ExamAPI } from '@/api/examAPI.ts';
import { useMutation } from '@tanstack/react-query';
import toast from 'react-hot-toast';

const useUnlikeExam = (options: UseMutationOptionsOf<typeof ExamAPI.unlike> = {}) => {
  return useMutation({
    onError: (error) => {
      const message = error.response?.data?.detail || '좋아요 취소에 실패했습니다.';
      toast.error(message);
    },
    mutationFn: ExamAPI.unlike,
    ...options,
  });
};

export default useUnlikeExam;
