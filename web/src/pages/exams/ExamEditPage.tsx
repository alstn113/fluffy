import { useNavigate, useParams } from 'react-router-dom';
import BaseLayout from '@/components/layouts/BaseLayout';
import styled from '@emotion/styled';
import ExamEditorSidebar from '@/components/questions/editors/ExamEditorSidebar.tsx';
import QuestionEditorTemplate from '@/components/questions/editors/QuestionEditorTemplate';
import QuestionTypeSelector from '@/components/questions/editors/QuestionTypeSelector';
import useExamEditorStore from '@/stores/useExamEditorStore';
import usePublishExam from '@/hooks/api/exam/usePublishExam';

const ExamEditPage = () => {
  const { id } = useParams() as { id: string };
  const { questionTypeSelectorActive, questions } = useExamEditorStore();
  const { mutate } = usePublishExam();
  const navigate = useNavigate();

  const handlePublish = () => {
    mutate(
      {
        examId: id,
        request: {
          questions,
        },
      },
      {
        onSuccess: () => {
          navigate(`/`);
        },
      },
    );
  };

  return (
    <BaseLayout>
      <Container>
        <ExamEditorSidebar />
        <MainContent>
          <Title>
            ExamEditPage {id} <SubmitButton onClick={handlePublish}>제출</SubmitButton>
          </Title>

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
  margin-bottom: 10px;
`;

const SubmitButton = styled.button`
  padding: 10px 20px;
  background-color: #4caf50; /* Green background */
  color: white; /* White text */
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;

  &:hover {
    background-color: #45a049; /* Darker green on hover */
  }

  &:disabled {
    background-color: #ccc; /* Gray background when disabled */
    cursor: not-allowed;
  }
`;

export default ExamEditPage;
