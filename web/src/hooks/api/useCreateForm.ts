import { FormAPI } from '~/api/formAPI';
import { UseMutationOptionsOf } from './types';
import { useMutation } from '@tanstack/react-query';

const useCreateForm = (options: UseMutationOptionsOf<typeof FormAPI.create> = {}) => {
  return useMutation({
    ...options,
    mutationFn: FormAPI.create,
  });
};

export default useCreateForm;
