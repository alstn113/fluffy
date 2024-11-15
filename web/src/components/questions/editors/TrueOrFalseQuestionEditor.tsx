import useExamEditorStore from '~/stores/useExamEditorStore';
import { TrueOrFalseQuestionRequest } from '~/api/questionAPI';
import styled from '@emotion/styled';

const TrueOrFalseQuestionEditor = () => {
  const { handleUpdateQuestion, currentIndex, questions } = useExamEditorStore();
  const question = questions[currentIndex] as TrueOrFalseQuestionRequest;

  const handleUpdateQuestionText = (text: string) => {
    const newQuestion: TrueOrFalseQuestionRequest = {
      ...question,
      text,
    };
    handleUpdateQuestion(currentIndex, newQuestion);
  };

  const handleUpdateCorrectAnswer = (isTrue: boolean) => {
    const newQuestion: TrueOrFalseQuestionRequest = {
      ...question,
      trueOrFalse: isTrue,
    };
    handleUpdateQuestion(currentIndex, newQuestion);
  };

  return (
    <Container>
      <Label>질문:</Label>
      <Input
        type="text"
        value={question.text}
        onChange={(e) => handleUpdateQuestionText(e.target.value)}
      />
      <Label>정답:</Label>
      <ButtonContainer>
        <Button
          className={`true ${question.trueOrFalse ? 'selected' : ''}`}
          onClick={() => handleUpdateCorrectAnswer(true)}
        >
          참
        </Button>
        <Button
          className={`false ${!question.trueOrFalse ? 'selected' : ''}`}
          onClick={() => handleUpdateCorrectAnswer(false)}
        >
          거짓
        </Button>
      </ButtonContainer>
    </Container>
  );
};

// 스타일 컴포넌트
const Container = styled.div`
  display: flex;
  flex-direction: column;
  padding: 16px;
  background: #f9f9f9;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
`;

const Label = styled.label`
  margin: 8px 0 4px;
`;

const Input = styled.input`
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-bottom: 12px;
`;

const ButtonContainer = styled.div`
  display: flex;
  width: 50%;
`;

const Button = styled.button`
  flex: 1; /* 버튼을 동일한 크기로 만듭니다 */
  padding: 1rem;
  margin: 0 0.5rem; /* 버튼 사이의 간격 */
  border: none;
  border-radius: 8px;
  color: #333;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s, transform 0.2s, box-shadow 0.2s;

  &.true {
    background-color: #d4edda; /* '참' 버튼 기본 색상 */
  }

  &.false {
    background-color: #f8d7da; /* '거짓' 버튼 기본 색상 */
  }

  &:hover {
    background-color: #c3e6cb; /* '참' 버튼 호버 색상 */
  }

  &:hover.false {
    background-color: #f5c6cb; /* '거짓' 버튼 호버 색상 */
  }

  &.selected {
    color: white; /* 텍스트 색상 변경 */
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2); /* 그림자 추가 */
    transform: scale(1.05); /* 약간 커지는 효과 */
  }

  &.selected.true {
    background-color: #28a745; /* 선택된 '참' 버튼 색상 */
  }

  &.selected.false {
    background-color: #dc3545; /* 선택된 '거짓' 버튼 색상 */
  }

  &:active {
    transform: translateY(1px); /* 클릭 시 약간 눌리는 효과 */
  }
`;

export default TrueOrFalseQuestionEditor;
