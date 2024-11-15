import { QuestionType } from '~/api/questionAPI';
import ShortAnswerQuestionEditor from './ShortAnswerQuestionEditor';
import LongAnswerQuestionEditor from './LongAnswerQuestionEditor';
import SingleChoiceQuestionEditor from './SingleChoiceQuestionEditor';
import MultipleChoiceQuestionEditor from './MultipleChoiceQuestionEditor';
import TrueOrFalseQuestionEditor from './TrueOrFalseQuestionEditor';
import React from 'react';
import useExamEditorStore from '~/stores/useExamEditorStore';
import styled from '@emotion/styled';

const QuestionEditorTemplate = () => {
  const { currentIndex, questions, handleUpdateQuestion } = useExamEditorStore();
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

  return (
    <div>
      <div>Text</div>
      <Input type="text" value={question.text} onChange={(e) => handleUpdateText(e.target.value)} />
      {editorMap[question.type]}
    </div>
  );
};

const Input = styled.input`
  padding: 8px;
  margin-bottom: 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
`;

export default QuestionEditorTemplate;
