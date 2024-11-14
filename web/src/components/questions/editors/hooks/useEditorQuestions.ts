import { useState } from 'react';
import {
  LongAnswerQuestionRequest,
  MultipleChoiceQuestionRequest,
  QuestionBaseRequest,
  QuestionType,
  ShortAnswerQuestionRequest,
  SingleChoiceQuestionRequest,
  TrueOrFalseQuestionRequest,
} from '~/api/questionAPI.ts';
import { produce } from 'immer';

interface Editor {
  currentIndex: number;
  questions: QuestionBaseRequest[];
  questionTypeSelectorActive: boolean;
}

const UseEditorQuestions = () => {
  const [editor, setEditor] = useState<Editor>({
    currentIndex: 0,
    questions: [],
    questionTypeSelectorActive: true,
  });

  const addQuestion = (newQuestion: QuestionBaseRequest) => {
    setEditor(
      produce((draft) => {
        draft.questions.push(newQuestion);
        draft.currentIndex = draft.questions.length - 1; // 현재 인덱스를 새로 추가된 질문으로 설정
        draft.questionTypeSelectorActive = false;
      }),
    );
  };

  const handleAddQuestion = (type: QuestionType) => {
    let newQuestion: QuestionBaseRequest;

    switch (type) {
      case 'SHORT_ANSWER':
        newQuestion = { text: type, correctAnswer: '', type } as ShortAnswerQuestionRequest;
        break;
      case 'LONG_ANSWER':
        newQuestion = { text: type, type } as LongAnswerQuestionRequest;
        break;
      case 'SINGLE_CHOICE':
        newQuestion = {
          text: type,
          type,
          options: ['옵션 1'],
          correctAnswerNumber: 0,
        } as SingleChoiceQuestionRequest; // 0부터 시작하는 인덱스
        break;
      case 'MULTIPLE_CHOICE':
        newQuestion = {
          text: type,
          type,
          options: ['옵션 1'],
          correctAnswerNumbers: [0],
        } as MultipleChoiceQuestionRequest; // 0부터 시작하는 인덱스
        break;
      case 'TRUE_OR_FALSE':
        newQuestion = { text: type, type, trueOrFalse: true } as TrueOrFalseQuestionRequest;
        break;
      default:
        return; // 잘못된 질문 타입일 경우 아무것도 하지 않음
    }

    addQuestion(newQuestion);
  };

  const handleDeleteQuestion = (index: number) => {
    setEditor(
      produce((draft) => {
        draft.questions.splice(index, 1);
        // 삭제 후 현재 인덱스를 조정 (삭제된 질문이 현재 인덱스보다 크면 1 감소)
        if (draft.currentIndex >= index) {
          draft.currentIndex = Math.max(0, draft.currentIndex - 1);
        }
      }),
    );
  };

  const handleUpdateQuestion = (index: number, question: QuestionBaseRequest) => {
    setEditor(
      produce((draft) => {
        draft.questions[index] = question;
      }),
    );
  };

  const handleQuestionTypeSelectorActive = () => {
    setEditor(
      produce((draft) => {
        draft.questionTypeSelectorActive = true;
      }),
    );
  };

  const handleSelectQuestion = (index: number) => {
    setEditor(
      produce((draft) => {
        draft.currentIndex = index;
        draft.questionTypeSelectorActive = false;
      }),
    );
  };

  const handleReorderQuestions = (newOrder: QuestionBaseRequest[]) => {
    setEditor(
      produce((draft) => {
        draft.questions = newOrder; // 새로운 순서로 질문 배열 업데이트
        draft.currentIndex = Math.min(draft.currentIndex, newOrder.length - 1); // 현재 인덱스 조정
      }),
    );
  };

  return {
    questions: editor.questions,
    currentIndex: editor.currentIndex,
    questionTypeSelectorActive: editor.questionTypeSelectorActive,
    handleAddQuestion,
    handleDeleteQuestion,
    handleUpdateQuestion,
    handleQuestionTypeSelectorActive,
    handleSelectQuestion,
    handleReorderQuestions,
  };
};

export default UseEditorQuestions;
