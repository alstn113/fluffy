import { SubmissionAPI } from '@/api/submissionAPI';
import { UseMutationOptionsOf } from '../types';
import { useMutation } from '@tanstack/react-query';

const useCreateExam = (options: UseMutationOptionsOf<typeof SubmissionAPI.submit> = {}) => {
  return useMutation({
    ...options,
    mutationFn: SubmissionAPI.submit,
  });
};

export default useCreateExam;
