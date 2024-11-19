import useExamEditorStore from '@/stores/useExamEditorStore';
import { TrueOrFalseQuestionRequest } from '@/api/questionAPI';

const TrueOrFalseQuestionEditor = () => {
  const { handleUpdateQuestion, currentIndex, questions } = useExamEditorStore();
  const question = questions[currentIndex] as TrueOrFalseQuestionRequest;

  const handleUpdateCorrectAnswer = (isTrue: boolean) => {
    const newQuestion: TrueOrFalseQuestionRequest = {
      ...question,
      trueOrFalse: isTrue,
    };
    handleUpdateQuestion(currentIndex, newQuestion);
  };

  return (
    <div className="flex flex-col p-4 bg-gray-100 rounded-md shadow-md">
      <label className="mb-2 text-lg font-semibold">정답:</label>
      <div className="flex w-1/2">
        <button
          className={`flex-1 p-4 mx-1 rounded-lg text-white transition duration-200 ${
            question.trueOrFalse ? 'bg-green-600' : 'bg-green-300 hover:bg-green-400'
          }`}
          onClick={() => handleUpdateCorrectAnswer(true)}
        >
          참
        </button>
        <button
          className={`flex-1 p-4 mx-1 rounded-lg text-white transition duration-200 ${
            !question.trueOrFalse ? 'bg-red-600' : 'bg-red-300 hover:bg-red-400'
          }`}
          onClick={() => handleUpdateCorrectAnswer(false)}
        >
          거짓
        </button>
      </div>
    </div>
  );
};

export default TrueOrFalseQuestionEditor;
