/** @jsxImportSource @emotion/react */
import { useState } from 'react';
import { useParams } from 'react-router-dom';
import BaseLayout from '~/components/layouts/BaseLayout';
import styled from '@emotion/styled';
import QuestionEditorSidebar from '~/components/questions/QuestionEditorSidebar';
import ProblemEditor from '~/components/questions/ProblemEditor';
import ProblemTypeSelector from '~/components/questions/ProblemTypeSelector';

const ExamEditPage = () => {
  const { id } = useParams() as { id: string };
  const [problems, setProblems] = useState([...Array(10).keys()].map((i) => `문제 ${i + 1}`));
  const [selectedProblem, setSelectedProblem] = useState<string | null>(null);
  const [showProblemTypeSelector, setShowProblemTypeSelector] = useState(false);

  const handleProblemClick = (problem: string) => {
    setSelectedProblem(problem);
  };

  const handleAddProblemType = () => {
    setShowProblemTypeSelector(true);
  };

  return (
    <BaseLayout>
      <Container>
        <QuestionEditorSidebar
          problems={problems}
          setProblems={setProblems}
          onProblemClick={handleProblemClick}
          onAddProblemType={handleAddProblemType}
        />
        <MainContent>
          <Title>ExamEditPage {id}</Title>
          {selectedProblem ? (
            <ProblemEditor problem={selectedProblem} onClose={() => setSelectedProblem(null)} />
          ) : (
            showProblemTypeSelector && (
              <ProblemTypeSelector onClose={() => setShowProblemTypeSelector(false)} />
            )
          )}
        </MainContent>
      </Container>
    </BaseLayout>
  );
};

const Container = styled.div`
  display: flex;
  height: 100vh; // 전체 높이를 차지하도록 설정
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

export default ExamEditPage;
