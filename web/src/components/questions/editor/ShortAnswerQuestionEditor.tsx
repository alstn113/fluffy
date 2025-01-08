import { ShortAnswerQuestionRequest } from '@/api/questionAPI';
import useExamEditorStore from '@/stores/useExamEditorStore';
import { Input } from '@nextui-org/react';

const ShortAnswerQuestionEditor = () => {
  const { handleUpdateQuestion, currentIndex, questions } = useExamEditorStore();
  const question = questions[currentIndex] as ShortAnswerQuestionRequest;

  const handleUpdateCorrectAnswer = (correctAnswer: string) => {
    const newQuestion: ShortAnswerQuestionRequest = { ...question, correctAnswer };
    handleUpdateQuestion(currentIndex, newQuestion);
  };

  return (
    <div className="mt-4">
      <Input
        value={question.correctAnswer}
        label="정답"
        variant="bordered"
        color="success"
        placeholder="정답을 입력하세요..."
        onValueChange={handleUpdateCorrectAnswer}
        className="w-full mb-2"
      />
    </div>
  );
};

export default ShortAnswerQuestionEditor;
