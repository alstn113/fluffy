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
    <div className="flex flex-col mt-8">
      <label className="mb-2 font-semibold">정답</label>
      <div className="flex w-1/2">
        <button
          className={`w-32 h-32 mx-4 rounded-full text-white font-bold transition-all duration-300 ${
            question.trueOrFalse
              ? 'bg-blue-600 transform scale-110'
              : 'bg-blue-300 hover:bg-blue-500'
          }`}
          onClick={() => handleUpdateCorrectAnswer(true)}
        >
          True
        </button>
        <button
          className={`w-32 h-32 mx-4 rounded-full text-white font-bold transition-all duration-300 ${
            !question.trueOrFalse ? 'bg-red-600 transform scale-110' : 'bg-red-300 hover:bg-red-500'
          }`}
          onClick={() => handleUpdateCorrectAnswer(false)}
        >
          False
        </button>
      </div>
    </div>
  );
};

export default TrueOrFalseQuestionEditor;
