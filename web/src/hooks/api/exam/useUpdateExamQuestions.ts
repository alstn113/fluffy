import { useMutation } from '@tanstack/react-query';
import { UseMutationOptionsOf } from '../types';
import { ExamAPI } from '@/api/examAPI';

const useUpdateExamQuestions = (
  options: UseMutationOptionsOf<typeof ExamAPI.updateQuestions> = {},
) => {
  return useMutation({
    ...options,
    mutationFn: ExamAPI.updateQuestions,
  });
};

export default useUpdateExamQuestions;
