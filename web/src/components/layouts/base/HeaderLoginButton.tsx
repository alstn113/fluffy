import { GITHUB_OAUTH_LOGIN_URL } from '@/constants';
import { Button, NavbarItem } from '@nextui-org/react';
import { useLocation } from 'react-router';

const HeaderLoginButton = () => {
  const location = useLocation();

  const handleGithubLogin = () => {
    window.location.href = `${GITHUB_OAUTH_LOGIN_URL}?next=${location.pathname}`;
  };

  return (
    <NavbarItem>
      <Button onPress={handleGithubLogin}>로그인</Button>
    </NavbarItem>
  );
};

export default HeaderLoginButton;
