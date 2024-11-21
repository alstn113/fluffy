import { QUESTION_TYPE, QuestionType } from '@/api/questionAPI';
import useExamEditorStore from '@/stores/useExamEditorStore';

const QuestionTypeSelector = () => {
  const { handleAddQuestion } = useExamEditorStore();

  const textQuestionType: Record<string, QuestionType> = {
    단답형: QUESTION_TYPE.shortAnswer,
    서술형: QUESTION_TYPE.longAnswer,
  };

  const choiceQuestionType: Record<string, QuestionType> = {
    '객관식(단일 선택)': QUESTION_TYPE.singleChoice,
    '객관식(복수 선택)': QUESTION_TYPE.multipleChoice,
    'True / False': QUESTION_TYPE.trueOrFalse,
  };

  return (
    <div className="flex flex-col gap-4">
      <h2 className="text-lg font-semibold text-gray-800 mb-4">문제 유형 선택</h2>

      <div>
        <h3 className="text-md font-semibold text-gray-500 mb-2">텍스트 질문 유형</h3>
        <div className="flex flex-wrap gap-2 mb-4">
          {Object.entries(textQuestionType).map(([typeName, type]) => (
            <button
              key={typeName}
              onClick={() => handleAddQuestion(type)}
              className="flex items-center justify-center px-4 py-2 text-white bg-purple-600 rounded-lg shadow hover:bg-purple-700 active:bg-purple-800 transition duration-300"
            >
              {typeName}
            </button>
          ))}
        </div>
      </div>

      <div>
        <h3 className="text-md font-semibold text-gray-500 mb-2">객관식 질문 유형</h3>
        <div className="flex flex-wrap gap-2">
          {Object.entries(choiceQuestionType).map(([typeName, type]) => (
            <button
              key={typeName}
              onClick={() => handleAddQuestion(type)}
              className="flex items-center justify-center px-4 py-2 text-white bg-purple-600 rounded-lg shadow hover:bg-purple-700 active:bg-purple-800 transition duration-300"
            >
              {typeName}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
};

export default QuestionTypeSelector;
