export const QUESTION_TYPE = {
  shortAnswer: 'SHORT_ANSWER',
  longAnswer: 'LONG_ANSWER',
  singleChoice: 'SINGLE_CHOICE',
  multipleChoice: 'MULTIPLE_CHOICE',
  trueOrFalse: 'TRUE_OR_FALSE',
} as const;

export type QuestionType = (typeof QUESTION_TYPE)[keyof typeof QUESTION_TYPE];
export type TextQuestionType = typeof QUESTION_TYPE.shortAnswer | typeof QUESTION_TYPE.longAnswer;
export type ChoiceQuestionType =
  | typeof QUESTION_TYPE.singleChoice
  | typeof QUESTION_TYPE.multipleChoice
  | typeof QUESTION_TYPE.trueOrFalse;

export type QuestionResponse = AnswerQuestionResponse | ChoiceQuestionResponse;

export type QuestionWithAnswersResponse =
  | AnswerQuestionWithAnswersResponse
  | ChoiceQuestionWithAnswersResponse;

export interface QuestionBaseResponse {
  id: number;
  text: string;
  passage: string;
  type: QuestionType;
}

export interface AnswerQuestionResponse extends QuestionBaseResponse {
  type: TextQuestionType;
}

export type AnswerQuestionWithAnswersResponse =
  | (QuestionBaseResponse & {
      type: typeof QUESTION_TYPE.shortAnswer;
      correctAnswer: string;
    })
  | (QuestionBaseResponse & {
      type: typeof QUESTION_TYPE.longAnswer;
    });

export interface ChoiceQuestionResponse extends QuestionBaseResponse {
  type: ChoiceQuestionType;
  options: QuestionOptionResponse[];
}

export interface ChoiceQuestionWithAnswersResponse extends ChoiceQuestionResponse {
  options: QuestionOptionWithAnswersResponse[];
}

export interface QuestionOptionResponse {
  id: string;
  text: string;
}

export interface QuestionOptionWithAnswersResponse extends QuestionOptionResponse {
  isCorrect: boolean;
}

export interface QuestionBaseRequest {
  text: string;
  passage: string;
  type: QuestionType;
}

export interface ShortAnswerQuestionRequest extends QuestionBaseRequest {
  type: typeof QUESTION_TYPE.shortAnswer;
  correctAnswer: string;
}

export interface LongAnswerQuestionRequest extends QuestionBaseRequest {
  type: typeof QUESTION_TYPE.longAnswer;
}

export interface SingleChoiceQuestionRequest extends QuestionBaseRequest {
  type: typeof QUESTION_TYPE.singleChoice;
  options: QuestionOptionRequest[];
}

export interface MultipleChoiceQuestionRequest extends QuestionBaseRequest {
  type: typeof QUESTION_TYPE.multipleChoice;
  options: QuestionOptionRequest[];
}

export interface TrueOrFalseQuestionRequest extends QuestionBaseRequest {
  type: typeof QUESTION_TYPE.trueOrFalse;
  trueOrFalse: boolean;
}

export interface QuestionOptionRequest {
  text: string;
  isCorrect: boolean;
}
