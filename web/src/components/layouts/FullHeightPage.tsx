import { css, Global } from '@emotion/react';
import styled from '@emotion/styled';

interface Props {
  children: React.ReactNode;
}
const FullHeightPage = ({ children }: Props) => {
  return (
    <>
      <Page>{children}</Page>
      <Global
        styles={css`
          html,
          body,
          #root {
            height: 100%;
          }
        `}
      />
    </>
  );
};

const Page = styled.div`
  height: 100%;
  display: flex;
  flex-direction: column;
`;

export default FullHeightPage;
