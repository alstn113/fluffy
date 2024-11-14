import { create } from 'zustand';
import {
  LongAnswerQuestionRequest,
  MultipleChoiceQuestionRequest,
  QuestionBaseRequest,
  QuestionType,
  ShortAnswerQuestionRequest,
  SingleChoiceQuestionRequest,
  TrueOrFalseQuestionRequest,
} from '~/api/questionAPI';
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
      return { text: type, correctAnswer: '', type } as ShortAnswerQuestionRequest;
    case 'LONG_ANSWER':
      return { text: type, type } as LongAnswerQuestionRequest;
    case 'SINGLE_CHOICE':
      return {
        text: type,
        type,
        options: ['옵션 1'],
        correctAnswerNumber: 0,
      } as SingleChoiceQuestionRequest;
    case 'MULTIPLE_CHOICE':
      return {
        text: type,
        type,
        options: ['옵션 1'],
        correctAnswerNumbers: [0],
      } as MultipleChoiceQuestionRequest;
    case 'TRUE_OR_FALSE':
      return {
        text: type,
        type,
        trueOrFalse: true,
      } as TrueOrFalseQuestionRequest;
  }
};

export default useExamEditorStore;
