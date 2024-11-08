import { useSuspenseQuery } from '@tanstack/react-query';
import { FormAPI } from '~/api/formAPI';
import { UseQueryOptionsOf } from './types';

const useGetForms = (options: UseQueryOptionsOf<typeof FormAPI.getAll> = {}) => {
  return useSuspenseQuery({
    ...options,
    queryKey: getKey(),
    queryFn: fetcher(),
  });
};

const getKey = () => ['forms'];
const fetcher = () => async () => await FormAPI.getAll();

useGetForms.getKey = getKey;
useGetForms.fetcher = fetcher;

export default useGetForms;
