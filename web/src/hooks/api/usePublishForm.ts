import { useMutation } from '@tanstack/react-query';
import { UseMutationOptionsOf } from './types';
import { FormAPI } from '~/api/formAPI';

const usePublishForm = (options: UseMutationOptionsOf<typeof FormAPI.publish> = {}) => {
  return useMutation({
    ...options,
    mutationFn: FormAPI.publish,
  });
};

export default usePublishForm;
