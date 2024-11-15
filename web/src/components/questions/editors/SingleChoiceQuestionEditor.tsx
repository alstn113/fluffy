import useExamEditorStore from '~/stores/useExamEditorStore';
import { SingleChoiceQuestionRequest } from '~/api/questionAPI';
import styled from '@emotion/styled';
import { Radio } from '~/components/common';

const SingleChoiceQuestionEditor = () => {
  const { handleUpdateQuestion, currentIndex, questions } = useExamEditorStore();
  const question = questions[currentIndex] as SingleChoiceQuestionRequest;

  // 옵션 추가
  // 옵션 제거 -> 정답 옵션 제거 시 정답은 고정 1로 설정
  // 옵션 텍스트 변경
  // 옵션 정답 변경
  // 옵션 순서 dnd로 변경하기

  const handleAddOption = () => {
    const newOption = { text: `옵션 ${question.options.length + 1}`, isCorrect: false };
    const newQuestion: SingleChoiceQuestionRequest = {
      ...question,
      options: [...question.options, newOption],
    };
    handleUpdateQuestion(currentIndex, newQuestion);
  };

  const handleRemoveOption = (index: number) => {
    const option = question.options[index];
    const isCorrectOption = option.isCorrect;

    const newOptions = question.options.filter((_, i) => i !== index);
    // 첫번째를 정답으로 만든다.
    if (isCorrectOption && newOptions.length > 0) {
      newOptions[0] = { ...newOptions[0], isCorrect: true };
    }

    const newQuestion: SingleChoiceQuestionRequest = { ...question, options: newOptions };
    handleUpdateQuestion(currentIndex, newQuestion);
  };

  const handleUpdateOptionText = (index: number, text: string) => {
    const newOptions = question.options.map((option, i) =>
      i === index ? { ...option, text } : option,
    );
    const newQuestion: SingleChoiceQuestionRequest = { ...question, options: newOptions };
    handleUpdateQuestion(currentIndex, newQuestion);
  };

  const handleUpdateOptionCorrect = (index: number) => {
    const newOptions = question.options.map((option, i) =>
      i === index ? { ...option, isCorrect: true } : { ...option, isCorrect: false },
    );
    const newQuestion: SingleChoiceQuestionRequest = { ...question, options: newOptions };
    handleUpdateQuestion(currentIndex, newQuestion);
  };

  return (
    <Container>
      <Label>선택지:</Label>

      {question.options.map((option, index) => (
        <Option key={index}>
          <Radio
            color="success"
            checked={option.isCorrect}
            onChange={() => handleUpdateOptionCorrect(index)}
          />
          <Input
            type="text"
            value={option.text}
            onChange={(e) => handleUpdateOptionText(index, e.target.value)}
          />
          <RemoveButton onClick={() => handleRemoveOption(index)}>삭제</RemoveButton>
        </Option>
      ))}

      <AddButton onClick={handleAddOption}>옵션 추가</AddButton>
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
`;

const Option = styled.div`
  display: flex;
  align-items: center;

  padding: 0.8rem; /* 패딩 조정 */
  background-color: #f7f7f7; /* 연한 배경색 */
  transition: background-color 0.2s, border-color 0.2s;

  &:hover {
    background-color: #e9ecef; /* 호버 시 배경색 변경 */
    border-color: #a0a0a0; /* 테두리 색상 변경 */
  }
`;

const RemoveButton = styled.button`
  margin-left: 8px;
  padding: 6px 12px;
  background: #ff4d4d;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;

  &:hover {
    background: #ff1a1a;
  }
`;

const AddButton = styled.button`
  margin-top: 12px;
  padding: 8px 12px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;

  &:hover {
    background: #0056b3;
  }
`;

export default SingleChoiceQuestionEditor;
