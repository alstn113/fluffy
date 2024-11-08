import styled from '@emotion/styled';
import { Link } from 'react-router-dom';

const Header = () => {
  const headerItems = [
    { name: 'Home', path: '/' },
    { name: 'Forms', path: '/forms' },
    { name: 'About', path: '/about' },
    { name: 'Edit', path: '/forms/1/edit' },
  ];

  return (
    <Container>
      <HeaderItems>
        {headerItems.map((item) => (
          <Link key={item.path} to={item.path}>
            {item.name}
          </Link>
        ))}
      </HeaderItems>
    </Container>
  );
};

const Container = styled.div`
  position: fixed;
  top: 0;
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

const HeaderItems = styled.div`
  flex: 1;
  display: flex;
  gap: 1rem;
  align-items: center;
  justify-content: center;
`;

export default Header;
