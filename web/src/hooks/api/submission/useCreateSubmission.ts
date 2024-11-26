import { SubmissionAPI } from '@/api/submissionAPI';
import { UseMutationOptionsOf } from '../types';
import { useMutation } from '@tanstack/react-query';
import toast from 'react-hot-toast';

const useCreateSubmission = (options: UseMutationOptionsOf<typeof SubmissionAPI.submit> = {}) => {
  return useMutation({
    onError: (error) => {
      const status = error.response?.status;
      const message = error.response?.data?.detail || '제출하는 중에 오류가 발생했습니다.';
      toast.error(`${status}: ${message}`);
    },
    mutationFn: SubmissionAPI.submit,
    ...options,
  });
};

export default useCreateSubmission;
