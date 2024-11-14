import { QuestionType } from '~/api/questionAPI';
import ShortAnswerQuestionEditor from './ShortAnswerQuestionEditor';
import LongAnswerQuestionEditor from './LongAnswerQuestionEditor';
import SingleChoiceQuestionEditor from './SingleChoiceQuestionEditor';
import MultipleChoiceQuestionEditor from './MultipleChoiceQuestionEditor';
import TrueOrFalseQuestionEditor from './TrueOrFalseQuestionEditor';
import React from 'react';
import useExamEditorStore from '~/stores/useExamEditorStore';

const QuestionEditorTemplate = () => {
  const { currentIndex, questions } = useExamEditorStore();
  const question = questions[currentIndex];

  const editorMap: Record<QuestionType, React.ReactNode> = {
    SHORT_ANSWER: <ShortAnswerQuestionEditor />,
    LONG_ANSWER: <LongAnswerQuestionEditor />,
    SINGLE_CHOICE: <SingleChoiceQuestionEditor />,
    MULTIPLE_CHOICE: <MultipleChoiceQuestionEditor />,
    TRUE_OR_FALSE: <TrueOrFalseQuestionEditor />,
  };

  return (
    <div>
      <h1>Question Form</h1>
      <div>
        {currentIndex + 1}. {question.text}
      </div>
      {editorMap[question.type]}
    </div>
  );
};

export default QuestionEditorTemplate;
