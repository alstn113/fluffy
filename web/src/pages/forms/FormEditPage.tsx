/** @jsxImportSource @emotion/react */
import { Reorder } from 'framer-motion';
import { useState } from 'react';
import { useParams } from 'react-router-dom';
import BaseLayout from '~/components/layouts/BaseLayout';
import styled from '@emotion/styled';

const FormEditPage = () => {
  const { id } = useParams() as { id: string };
  const [problems, setProblems] = useState([...Array(10).keys()].map((i) => `문제 ${i + 1}`));

  return (
    <BaseLayout>
      <Container>
        <Sidebar>
          <h3>문제 목록</h3>
          <Reorder.Group axis="y" values={problems} onReorder={setProblems}>
            {problems.map((problem) => (
              <ProblemItem key={problem} value={problem}>
                {problem}
              </ProblemItem>
            ))}
          </Reorder.Group>
        </Sidebar>
        <MainContent>
          <Title>FormEditPage {id}</Title>
        </MainContent>
      </Container>
    </BaseLayout>
  );
};

const Container = styled.div`
  display: flex;
  height: 100vh; // 전체 높이를 차지하도록 설정
`;

const Sidebar = styled.div`
  width: 200px; // 사이드바의 너비
  padding: 10px;
  background: #f5f5f5;
  border-right: 1px solid #ddd; // 오른쪽 경계선
`;

const MainContent = styled.div`
  flex: 1; // 남은 공간을 차지
  padding: 10px;
  background: #ffffff;
`;

const Title = styled.div`
  font-size: 24px;
  margin-bottom: 20px;
`;

const ProblemItem = styled(Reorder.Item)`
  background: #d7d7d7;
  padding: 12px;
  margin: 8px 0;
  border-radius: 4px;
  cursor: grab;
`;

export default FormEditPage;
