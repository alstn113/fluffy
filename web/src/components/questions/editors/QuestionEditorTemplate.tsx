import { QuestionType } from '@/api/questionAPI';
import ShortAnswerQuestionEditor from './ShortAnswerQuestionEditor';
import LongAnswerQuestionEditor from './LongAnswerQuestionEditor';
import SingleChoiceQuestionEditor from './SingleChoiceQuestionEditor';
import MultipleChoiceQuestionEditor from './MultipleChoiceQuestionEditor';
import TrueOrFalseQuestionEditor from './TrueOrFalseQuestionEditor';
import React from 'react';
import useExamEditorStore from '@/stores/useExamEditorStore';

const QuestionEditorTemplate = () => {
  const {
    currentIndex,
    questions,
    handleUpdateQuestion,
    handleDeleteQuestion,
    setQuestionTypeSelectorActive,
  } = useExamEditorStore();
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
    <div className="p-4">
      <div className="mb-2 font-semibold">질문</div>
      <input
        type="text"
        value={question.text}
        onChange={(e) => handleUpdateText(e.target.value)}
        className="w-full p-2 border border-gray-300 rounded-md mb-3"
      />
      <button
        onClick={handleDeleteQuestionClick}
        className="px-4 py-2 border border-gray-300 rounded-md bg-red-600 text-white hover:bg-red-700 transition duration-200"
      >
        Delete
      </button>
      {editorMap[question.type]}
    </div>
  );
};

export default QuestionEditorTemplate;
