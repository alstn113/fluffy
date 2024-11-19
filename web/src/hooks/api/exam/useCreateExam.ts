import { ExamAPI } from '@/api/examAPI';
import { UseMutationOptionsOf } from '../types';
import { useMutation } from '@tanstack/react-query';

const useCreateExam = (options: UseMutationOptionsOf<typeof ExamAPI.create> = {}) => {
  return useMutation({
    ...options,
    mutationFn: ExamAPI.create,
  });
};

export default useCreateExam;
