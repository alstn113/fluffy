import { ShortAnswerQuestionRequest } from '@/api/questionAPI';
import useExamEditorStore from '@/stores/useExamEditorStore';
import { Input } from '@heroui/react';

const ShortAnswerQuestionView = () => {
  const { currentIndex, questions } = useExamEditorStore();
  const question = questions[currentIndex] as ShortAnswerQuestionRequest;

  return (
    <div className="mt-4">
      <Input
        isReadOnly
        value={question.correctAnswer}
        label="정답"
        variant="bordered"
        color="success"
        className="w-full mb-2"
      />
    </div>
  );
};

export default ShortAnswerQuestionView;
