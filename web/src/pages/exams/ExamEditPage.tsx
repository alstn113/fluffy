import { useParams } from 'react-router-dom';
import BaseLayout from '~/components/layouts/BaseLayout';
import styled from '@emotion/styled';
import ExamEditorSidebar from '~/components/questions/editors/ExamEditorSidebar.tsx';
import QuestionEditorTemplate from '~/components/questions/editors/QuestionEditorTemplate';
import QuestionTypeSelector from '~/components/questions/editors/QuestionTypeSelector';
import useExamEditorStore from '~/stores/useExamEditorStore';

const ExamEditPage = () => {
  const { id } = useParams() as { id: string };
  const { questionTypeSelectorActive } = useExamEditorStore();

  return (
    <BaseLayout>
      <Container>
        <ExamEditorSidebar />
        <MainContent>
          <Title>ExamEditPage {id}</Title>
          {questionTypeSelectorActive ? <QuestionTypeSelector /> : <QuestionEditorTemplate />}
        </MainContent>
      </Container>
    </BaseLayout>
  );
};

const Container = styled.div`
  display: flex;
  height: 100vh;
`;

const MainContent = styled.div`
  flex: 1;
  padding: 10px;
  background: #ffffff;
`;

const Title = styled.div`
  font-size: 24px;
  margin-bottom: 20px;
`;

export default ExamEditPage;
