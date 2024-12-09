import { QuestionType } from '@/api/questionAPI';
import ShortAnswerQuestionEditor from './ShortAnswerQuestionEditor';
import LongAnswerQuestionEditor from './LongAnswerQuestionEditor';
import SingleChoiceQuestionEditor from './SingleChoiceQuestionEditor';
import MultipleChoiceQuestionEditor from './MultipleChoiceQuestionEditor';
import TrueOrFalseQuestionEditor from './TrueOrFalseQuestionEditor';
import React from 'react';
import useExamEditorStore from '@/stores/useExamEditorStore';
import { Button, Input } from '@nextui-org/react';

const QuestionEditorTemplate = () => {
  const { currentIndex, questions, handleUpdateQuestion, handleDeleteQuestion, setQuestionTypeSelectorActive } =
    useExamEditorStore();
  const question = questions[currentIndex];

  const editorMap: Record<QuestionType, React.ReactNode> = {
    SHORT_ANSWER: <ShortAnswerQuestionEditor />,
    LONG_ANSWER: <LongAnswerQuestionEditor />,
    SINGLE_CHOICE: <SingleChoiceQuestionEditor />,
    MULTIPLE_CHOICE: <MultipleChoiceQuestionEditor />,
    TRUE_OR_FALSE: <TrueOrFalseQuestionEditor />,
  };

  const handleUpdateText = (text: string) => {
    const newQuestion = { ...question, text };
    handleUpdateQuestion(currentIndex, newQuestion);
  };

  const handleDeleteQuestionClick = () => {
    if (questions.length === 1) {
      setQuestionTypeSelectorActive(true);
    }
    handleDeleteQuestion(currentIndex);
  };

  return (
    <div>
      <div className="flex gap-2 justify-between items-center mb-4">
        <div className=" text-2xl font-bold mb-2">{currentIndex + 1}번째 질문</div>
        <Button color="danger" variant="shadow" onPress={handleDeleteQuestionClick}>
          질문 삭제
        </Button>
      </div>
      <Input
        value={question.text}
        label="질문"
        variant="underlined"
        placeholder="질문을 입력하세요..."
        onValueChange={handleUpdateText}
        className="w-full mb-2"
        classNames={{
          label: 'text-xl font-semibold text-gray-800',
        }}
      />

      {editorMap[question.type]}
    </div>
  );
};

export default QuestionEditorTemplate;
