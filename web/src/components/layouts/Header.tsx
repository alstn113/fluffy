import styled from '@emotion/styled';
import { Link } from 'react-router-dom';
import { PAGE_LIST } from '~/constants';

const Header = () => {
  const headerItems = [
    { name: 'Home', path: PAGE_LIST.home },
    { name: 'Exams', path: PAGE_LIST.exam.list },
    { name: 'About', path: PAGE_LIST.about },
    { name: 'Edit', path: PAGE_LIST.exam.edit },
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
    padding: 0 16px;

    display: flex;
    align-items: center;
    margin-bottom: 90px;

    background-color: rgba(255, 255, 255, 0.6);
    backdrop-filter: saturate(180%) blur(10px);
    box-shadow: 0 5px 20px -5px rgba(2, 1, 1, 0.1);
`;

const HeaderItems = styled.div`
  flex: 1;
  display: flex;
  gap: 1rem;
  align-items: center;
  justify-content: center;
`;

export default Header;
