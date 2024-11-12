import { useQueryClient } from '@tanstack/react-query';
import { MyInfoResponse } from '~/api/authAPI';
import useGetMe from './useGetMe';

const useUser = () => {
  const queryClient = useQueryClient();
  const user = queryClient.getQueryData<MyInfoResponse>(useGetMe.getkey());

  return user as MyInfoResponse;
};

export default useUser;
