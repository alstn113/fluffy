import { create } from 'zustand';
import {
  LongAnswerQuestionRequest,
  MultipleChoiceQuestionRequest,
  QuestionBaseRequest,
  QuestionType,
  ShortAnswerQuestionRequest,
  SingleChoiceQuestionRequest,
  TrueOrFalseQuestionRequest,
} from '@/api/questionAPI';
import { immer } from 'zustand/middleware/immer';

type States = {
  questions: QuestionBaseRequest[];
  currentIndex: number;
  questionTypeSelectorActive: boolean;
};

type Actions = {
  setQuestions: (questions: QuestionBaseRequest[]) => void;
  setCurrentIndex: (index: number) => void;
  setQuestionTypeSelectorActive: (isActive: boolean) => void;
  handleAddQuestion: (type: QuestionType) => void;
  handleDeleteQuestion: (index: number) => void;
  handleMoveQuestion: (from: number, to: number) => void;
  handleUpdateQuestion: (index: number, question: QuestionBaseRequest) => void;
  handleSelectQuestion: (index: number) => void;
};

const useExamEditorStore = create<States & Actions>()(
  immer((set) => ({
    questions: [],
    currentIndex: 0,
    questionTypeSelectorActive: true,

    setQuestions: (questions) =>
      set((state) => {
        state.questions = questions;
      }),
    setCurrentIndex: (index) =>
      set((state) => {
        state.currentIndex = index;
      }),
    setQuestionTypeSelectorActive: (isActive) =>
      set((state) => {
        state.questionTypeSelectorActive = isActive;
      }),
    handleAddQuestion: (type) => {
      const newQuestion = createQuestion(type);
      set((state) => {
        state.questions.push(newQuestion);
        state.currentIndex = state.questions.length - 1;
        state.questionTypeSelectorActive = false;
      });
    },
    handleDeleteQuestion: (index) =>
      set((state) => {
        state.questions.splice(index, 1);
        state.currentIndex = Math.min(state.currentIndex, state.questions.length - 1);
      }),
    handleMoveQuestion: (from, to) =>
      set((state) => {
        const [question] = state.questions.splice(from, 1);
        state.questions.splice(to, 0, question);
        state.currentIndex = to;
      }),
    handleUpdateQuestion: (index, question) =>
      set((state) => {
        state.questions[index] = question;
      }),
    handleSelectQuestion: (index) =>
      set((state) => {
        state.currentIndex = index;
        state.questionTypeSelectorActive = false;
      }),
  })),
);

const createQuestion = (type: QuestionType): QuestionBaseRequest => {
  switch (type) {
    case 'SHORT_ANSWER':
      return { text: '단답형 질문', correctAnswer: '', type } as ShortAnswerQuestionRequest;
    case 'LONG_ANSWER':
      return { text: '서술형 질문', type } as LongAnswerQuestionRequest;
    case 'SINGLE_CHOICE':
      return {
        text: '객관식 단일 선택 질문',
        type,
        options: [
          {
            text: '옵션 1',
            isCorrect: true,
          },
          {
            text: '옵션 2',
            isCorrect: false,
          },
        ],
      } as SingleChoiceQuestionRequest;
    case 'MULTIPLE_CHOICE':
      return {
        text: '객관식 복수 선택 질문',
        type,
        options: [
          {
            text: '옵션 1',
            isCorrect: true,
          },
          {
            text: '옵션 2',
            isCorrect: false,
          },
        ],
      } as MultipleChoiceQuestionRequest;
    case 'TRUE_OR_FALSE':
      return {
        text: 'True / False 질문',
        type,
        trueOrFalse: true,
      } as TrueOrFalseQuestionRequest;
  }
};

export default useExamEditorStore;
