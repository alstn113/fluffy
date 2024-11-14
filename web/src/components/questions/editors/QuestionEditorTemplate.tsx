import { QuestionBaseRequest, QuestionType } from '~/api/questionAPI';
import ShortAnswerQuestionEditor from './ShortAnswerQuestionEditor';
import LongAnswerQuestionEditor from './LongAnswerQuestionEditor';
import SingleChoiceQuestionEditor from './SingleChoiceQuestionEditor';
import MultipleChoiceQuestionEditor from './MultipleChoiceQuestionEditor';
import TrueOrFalseQuestionEditor from './TrueOrFalseQuestionEditor';
import React from 'react';

interface QuestionEditorTemplateProps {
  currentIndex: number;
  question: QuestionBaseRequest;
  onUpdateQuestion: (currentIndex: number, question: QuestionBaseRequest) => void;
}

const QuestionEditorTemplate = ({
  currentIndex,
  question,
  onUpdateQuestion,
}: QuestionEditorTemplateProps) => {
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
