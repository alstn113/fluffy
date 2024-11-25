import { create } from 'zustand';
import {
  LongAnswerQuestionRequest,
  MultipleChoiceQuestionRequest,
  QuestionBaseRequest,
  QuestionType,
  QuestionWithAnswersResponse,
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
  initialize: (questions: QuestionWithAnswersResponse[]) => void;
  setQuestions: (questions: QuestionBaseRequest[]) => void;
  setCurrentIndex: (index: number) => void;
  setQuestionTypeSelectorActive: (isActive: boolean) => void;
  clear: () => void;

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

    initialize: (questions) => {
      const questionRequests = initializeQuestions(questions);
      set((state) => {
        state.questions = questionRequests;
        state.currentIndex = 0;
        state.questionTypeSelectorActive = true;

        if (state.questions.length > 0) {
          state.currentIndex = 0;
          state.questionTypeSelectorActive = false;
        }
      });
    },
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
    clear: () =>
      set((state) => {
        state.questions = [];
        state.currentIndex = 0;
        state.questionTypeSelectorActive = true;
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

const initializeQuestions = (questions: QuestionWithAnswersResponse[]): QuestionBaseRequest[] => {
  const newQuestions = questions.map((question) => {
    switch (question.type) {
      case 'SHORT_ANSWER':
        return {
          text: question.text,
          correctAnswer: question.correctAnswer,
          type: question.type,
        } as ShortAnswerQuestionRequest;
      case 'LONG_ANSWER':
        return {
          text: question.text,
          type: question.type,
        } as LongAnswerQuestionRequest;
      case 'SINGLE_CHOICE':
        return {
          text: question.text,
          type: question.type,
          options: question.options.map((option) => ({
            text: option.text,
            isCorrect: option.isCorrect,
          })),
        } as SingleChoiceQuestionRequest;
      case 'MULTIPLE_CHOICE':
        return {
          text: question.text,
          type: question.type,
          options: question.options.map((option) => ({
            text: option.text,
            isCorrect: option.isCorrect,
          })),
        } as MultipleChoiceQuestionRequest;
      case 'TRUE_OR_FALSE':
        return {
          text: question.text,
          type: question.type,
          trueOrFalse: question.options[0].isCorrect,
        } as TrueOrFalseQuestionRequest;
    }
  });

  return newQuestions;
};

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
