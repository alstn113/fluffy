import { useQuery } from '@tanstack/react-query';
import { AuthAPI } from '@/api/authAPI';

const useGetMe = () => {
  return useQuery({
    queryKey: getKey(),
    queryFn: fetcher(),
  });
};

const getKey = () => ['useGetMe'];
const fetcher = () => async () => await AuthAPI.getMyInfo();

useGetMe.getkey = getKey;
useGetMe.fetcher = fetcher;

export default useGetMe;
