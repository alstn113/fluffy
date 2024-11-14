import { useParams } from 'react-router-dom';
import BaseLayout from '~/components/layouts/BaseLayout';
import styled from '@emotion/styled';
import ExamEditorSidebar from '~/components/questions/editors/ExamEditorSidebar.tsx';
import UseEditorQuestions from '~/components/questions/editors/hooks/useEditorQuestions.ts';
import QuestionEditorTemplate from '~/components/questions/editors/QuestionEditorTemplate';
import QuestionTypeSelector from '~/components/questions/editors/QuestionTypeSelector';

const ExamEditPage = () => {
  const { id } = useParams() as { id: string };
  const {
    questions,
    currentIndex,
    questionTypeSelectorActive,
    handleAddQuestion,
    handleDeleteQuestion,
    handleUpdateQuestion,
    handleSelectQuestion,
    handleQuestionTypeSelectorActive,
    handleReorderQuestions,
  } = UseEditorQuestions();

  return (
    <BaseLayout>
      <Container>
        <ExamEditorSidebar
          questions={questions}
          currentIndex={currentIndex}
          questionTypeSelectorActive={questionTypeSelectorActive}
          onQuestionTypeSelectorActive={handleQuestionTypeSelectorActive}
          onSelectQuestion={handleSelectQuestion}
          onReorderQuestions={handleReorderQuestions}
        />
        <MainContent>
          <Title>ExamEditPage {id}</Title>
          {questionTypeSelectorActive ? (
            <QuestionTypeSelector onAddQuestion={handleAddQuestion} />
          ) : (
            <QuestionEditorTemplate
              currentIndex={currentIndex}
              question={questions[currentIndex]}
              onUpdateQuestion={handleUpdateQuestion}
            />
          )}
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
