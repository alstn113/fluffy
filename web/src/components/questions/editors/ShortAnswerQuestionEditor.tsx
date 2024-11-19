import styled from '@emotion/styled';
import { ShortAnswerQuestionRequest } from '@/api/questionAPI';
import useExamEditorStore from '@/stores/useExamEditorStore';

const ShortAnswerQuestionEditor = () => {
  const { handleUpdateQuestion, currentIndex, questions } = useExamEditorStore();
  const question = questions[currentIndex] as ShortAnswerQuestionRequest;

  const handleUpdateCorrectAnswer = (correctAnswer: string) => {
    const newQuestion: ShortAnswerQuestionRequest = { ...question, correctAnswer };
    handleUpdateQuestion(currentIndex, newQuestion);
  };

  return (
    <div>
      <div>
        <label>Correct Answer </label>
        <Input
          type="text"
          value={question.correctAnswer}
          onChange={(e) => handleUpdateCorrectAnswer(e.target.value)}
        />
      </div>
    </div>
  );
};

const Input = styled.input`
  padding: 8px;
  margin-bottom: 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
`;

export default ShortAnswerQuestionEditor;
