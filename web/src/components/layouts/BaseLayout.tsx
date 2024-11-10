import styled from '@emotion/styled';
import FullHeightPage from './FullHeightPage';
import Header from './Header';

interface BaseLayoutProps {
  children: React.ReactNode;
}

const BaseLayout = ({ children }: BaseLayoutProps) => {
  return (
    <FullHeightPage>
      <Header />
      <Content>{children}</Content>
    </FullHeightPage>
  );
};

const Content = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
  overflow: scroll;
  overflow-x: hidden;
  background: #fff;
  padding-top: 4rem;
`;

export default BaseLayout;
