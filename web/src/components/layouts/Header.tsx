import styled from '@emotion/styled';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button } from '~/components/common';
import { GITHUB_OAUTH_LOGIN_URL, PAGE_LIST } from '~/constants';
import HeaderDropdown from './HeaderMenu/HeaderMenu';
import useLogout from '~/hooks/useLogout';
import useGetMe from '~/hooks/useGetMe';

const Header = () => {
  const { data: user } = useGetMe();

  const navigate = useNavigate();
  const logout = useLogout();
  const location = useLocation();

  const handleGithubLogin = () => {
    window.location.href = `${GITHUB_OAUTH_LOGIN_URL}?next=${location.pathname}`;
  };

  const menuItemList = [
    {
      text: 'Home',
      onClick: () => navigate(`${PAGE_LIST.home}`),
      red: false,
    },
    {
      text: 'About',
      onClick: () => navigate(`${PAGE_LIST.about}`),
      red: false,
    },
    {
      text: 'Exams',
      onClick: () => navigate(`${PAGE_LIST.exam.list}`),
      red: false,
    },
    {
      text: 'Logout',
      onClick: logout,
      red: true,
    },
  ];

  return (
    <Container>
      <LogoLink to="/">Pass</LogoLink>
      <HeaderItems>
        {user ? (
          <HeaderDropdown menuItemList={menuItemList} />
        ) : (
          <Button shadow color="primary" size="sm" onClick={handleGithubLogin}>
            Login
          </Button>
        )}
      </HeaderItems>
    </Container>
  );
};

const Container = styled.header`
  position: fixed;
  top: 0;
  z-index: 10;
  width: 100%;
  height: 4rem;
  padding: 0px 16px;

  display: flex;
  align-items: center;
  padding: 0px 16px;
  margin-bottom: 90px;

  background-color: rgba(255, 255, 255, 0.6);
  backdrop-filter: saturate(180%) blur(10px);
  box-shadow: 0px 5px 20px -5px rgba(2, 1, 1, 0.1);
`;

const LogoLink = styled(Link)`
  display: flex;
  align-items: center;
  justify-content: center;

  font-family: 'PyeongChangPeace-Bold', sans-serif;
  font-size: 1.5rem;
  font-weight: 900;
  border-radius: 10px;
  color: #000000;
  padding: 0 16px;

  img {
    margin-right: 8px;
    width: 24px;
    height: 24px;
  }
`;

const HeaderItems = styled.div`
  flex: 1;
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  align-items: center;
`;

export default Header;
