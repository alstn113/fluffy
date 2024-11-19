import { QUESTION_TYPE } from '@/api/questionAPI';
import useExamEditorStore from '@/stores/useExamEditorStore';

const QuestionTypeSelector = () => {
  const { handleAddQuestion } = useExamEditorStore();

  return (
    <div className="flex flex-col gap-2 p-5 bg-gray-100 rounded-lg shadow-md">
      {Object.values(QUESTION_TYPE).map((type) => (
        <button
          key={type}
          onClick={() => handleAddQuestion(type)}
          className="px-4 py-2 text-white bg-blue-300 rounded-md hover:bg-blue-400 active:bg-blue-600 transition duration-300"
        >
          {type}
        </button>
      ))}
    </div>
  );
};

export default QuestionTypeSelector;
