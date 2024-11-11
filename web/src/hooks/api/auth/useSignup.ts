import { UseMutationOptionsOf } from '../types';
import { useMutation } from '@tanstack/react-query';
import { AuthAPI } from '~/api/authAPI';

const useLogin = (options: UseMutationOptionsOf<typeof AuthAPI.signup> = {}) => {
  return useMutation({
    ...options,
    mutationFn: AuthAPI.signup,
  });
};

export default useLogin;
