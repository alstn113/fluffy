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
    <div>
      <div>Text</div>
      <Input type="text" value={question.text} onChange={(e) => handleUpdateText(e.target.value)} />
      <DeleteButton onClick={handleDeleteQuestionClick}>Delete</DeleteButton>
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

const DeleteButton = styled.button`
  padding: 8px;
  margin-left: 8px;
  margin-bottom: 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
  background: #e54545;
  color: white;
`;

export default QuestionEditorTemplate;
