import { create } from 'zustand';
import {} from '@/api/questionAPI';
import { immer } from 'zustand/middleware/immer';
import { AnswerRequest } from '@/api/submissionAPI';

type States = {
  answers: AnswerRequest[];
};

type Actions = {
  initailize: (size: number) => void;
  clear: () => void;
  handleUpdateText: (index: number, text: string) => void;
  handleUpdateChoices: (index: number, choices: number[]) => void;
};

const useSubmissionStore = create<States & Actions>()(
  immer((set) => ({
    answers: [],

    initailize: (size) =>
      set((state) => {
        state.answers = Array.from({ length: size }, () => ({
          text: '',
          choices: [],
        }));
      }),
    clear: () =>
      set((state) => {
        state.answers = [];
      }),
    handleUpdateText: (index, text) => {
      set((state) => {
        state.answers[index].text = text;
      });
    },
    handleUpdateChoices: (index, choices) => {
      set((state) => {
        state.answers[index].choices = choices;
      });
    },
  })),
);

export default useSubmissionStore;
