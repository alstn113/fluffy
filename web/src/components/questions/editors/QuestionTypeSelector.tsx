import styled from '@emotion/styled';
import { QUESTION_TYPE } from '@/api/questionAPI';
import useExamEditorStore from '@/stores/useExamEditorStore';

const QuestionTypeSelector = () => {
  const { handleAddQuestion } = useExamEditorStore();

  return (
    <Container>
      {Object.values(QUESTION_TYPE).map((type) => (
        <StyledButton key={type} onClick={() => handleAddQuestion(type)}>
          {type}
        </StyledButton>
      ))}
    </Container>
  );
};

const Container = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
`;

const StyledButton = styled.button`
  padding: 10px 15px;
  font-size: 16px;
  color: white;
  background-color: #abc1d8;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;

  &:hover {
    background-color: #5684b6;
  }

  &:active {
    background-color: #004085;
  }
`;

export default QuestionTypeSelector;
