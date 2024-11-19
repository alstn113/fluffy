import { AuthAPI } from '@/api/authAPI';

const useLogout = () => {
  const logout = async () => {
    try {
      await AuthAPI.logout();
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
    } catch (error) {
      /* empty */
    }
    window.location.href = '/';
  };

  return logout;
};

export default useLogout;
