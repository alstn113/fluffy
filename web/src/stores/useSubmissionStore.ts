import { create } from 'zustand';
import { immer } from 'zustand/middleware/immer';
import { QuestionResponse } from '@/api/submissionAPI';

type States = {
  currentQuestionIndex: number;
  questionLength: number;
  questionResponses: QuestionResponse[];
};

type Actions = {
  initialize: (size: number) => void;
  clear: () => void;
  handleUpdateTextAnswer: (index: number, text: string) => void;
  handleUpdateChoiceAnswer: (index: number, choices: string[]) => void;
  moveNext: () => void;
  movePrev: () => void;
};

const useSubmissionStore = create<States & Actions>()(
  immer((set) => ({
    questionResponses: [],
    currentQuestionIndex: 0,
    questionLength: 0,

    initialize: (size) =>
      set((state) => {
        state.questionResponses = Array.from({ length: size }, () => ({
          answers: [],
        }));
        state.questionLength = size;
        state.currentQuestionIndex = 0;
      }),
    clear: () =>
      set((state) => {
        state.questionResponses = [];
      }),
    handleUpdateTextAnswer: (index, text) => {
      set((state) => {
        state.questionResponses[index].answers = [text];
      });
    },
    handleUpdateChoiceAnswer: (index, choices) => {
      set((state) => {
        state.questionResponses[index].answers = choices;
      });
    },
    moveNext: () =>
      set((state) => {
        if (state.currentQuestionIndex < state.questionLength - 1) {
          state.currentQuestionIndex += 1;
        }
      }),
    movePrev: () =>
      set((state) => {
        if (state.currentQuestionIndex > 0) {
          state.currentQuestionIndex -= 1;
        }
      }),
  })),
);

export default useSubmissionStore;
