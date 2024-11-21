import { create } from 'zustand';
import { immer } from 'zustand/middleware/immer';
import { QuestionResponse } from '@/api/submissionAPI';

type States = {
  questionResponses: QuestionResponse[];
};

type Actions = {
  initialize: (size: number) => void;
  clear: () => void;
  handleUpdateTextAnswer: (index: number, text: string) => void;
  handleUpdateChoiceAnswer: (index: number, choices: string[]) => void;
};

const useSubmissionStore = create<States & Actions>()(
  immer((set) => ({
    questionResponses: [],

    initialize: (size) =>
      set((state) => {
        state.questionResponses = Array.from({ length: size }, () => ({
          answers: [],
        }));
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
  })),
);

export default useSubmissionStore;
