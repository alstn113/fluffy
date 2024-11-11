import { useMutation } from '@tanstack/react-query';
import { UseMutationOptionsOf } from '../types';
import { ExamAPI } from '~/api/examAPI';

const usePublishExam = (options: UseMutationOptionsOf<typeof ExamAPI.publish> = {}) => {
  return useMutation({
    ...options,
    mutationFn: ExamAPI.publish,
  });
};

export default usePublishExam;
