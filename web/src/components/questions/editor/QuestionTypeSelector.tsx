import { QUESTION_TYPE, QuestionType } from '@/api/questionAPI';
import useExamEditorStore from '@/stores/useExamEditorStore';
import { Button } from '@nextui-org/react';

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
            <Button
              color="primary"
              variant="shadow"
              key={typeName}
              onPress={() => handleAddQuestion(type)}
            >
              {typeName}
            </Button>
          ))}
        </div>
      </div>

      <div>
        <h3 className="text-md font-semibold text-gray-500 mb-2">객관식 질문 유형</h3>
        <div className="flex flex-wrap gap-2">
          {Object.entries(choiceQuestionType).map(([typeName, type]) => (
            <Button
              color="primary"
              variant="shadow"
              key={typeName}
              onPress={() => handleAddQuestion(type)}
            >
              {typeName}
            </Button>
          ))}
        </div>
      </div>
    </div>
  );
};

export default QuestionTypeSelector;
