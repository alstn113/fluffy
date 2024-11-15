import { ShortAnswerQuestionRequest } from '~/api/questionAPI';
import useExamEditorStore from '~/stores/useExamEditorStore';

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
        <label>Correct Answer</label>
        <hr />
        <input
          type="text"
          value={question.correctAnswer}
          onChange={(e) => handleUpdateCorrectAnswer(e.target.value)}
        />
      </div>
    </div>
  );
};

export default ShortAnswerQuestionEditor;
