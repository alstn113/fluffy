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
    <div className="p-4">
      <div className="mb-4">
        <label className="block text-lg font-semibold">Correct Answer</label>
        <input
          type="text"
          value={question.correctAnswer}
          onChange={(e) => handleUpdateCorrectAnswer(e.target.value)}
          className="w-full p-2 border border-gray-300 rounded-md"
        />
      </div>
    </div>
  );
};

export default ShortAnswerQuestionEditor;
