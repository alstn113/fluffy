import { useSuspenseQuery } from '@tanstack/react-query';
import { FormAPI } from '~/api/formAPI';
import { UseQueryOptionsOf } from './types';

const useGetForm = (formId: string, options: UseQueryOptionsOf<typeof FormAPI.getById> = {}) => {
  return useSuspenseQuery({
    ...options,
    queryKey: getKey(formId),
    queryFn: fetcher(formId),
  });
};

const getKey = (formId: string) => ['forms', formId];
const fetcher = (formId: string) => async () => await FormAPI.getById(formId);

useGetForm.getKey = getKey;
useGetForm.fetcher = fetcher;

export default useGetForm;
