export const QUESTION_TYPE = {
  shortAnswer: 'SHORT_ANSWER',
  longAnswer: 'LONG_ANSWER',
  singleChoice: 'SINGLE_CHOICE',
  multipleChoice: 'MULTIPLE_CHOICE',
  trueOrFalse: 'TRUE_OR_FALSE',
} as const;
export type QuestionType = (typeof QUESTION_TYPE)[keyof typeof QUESTION_TYPE];

export type QuestionResponse = AnswerQuestionResponse | ChoiceQuestionResponse;

interface QuestionBaseResponse {
  id: number;
  text: string;
  sequence: number;
  type: QuestionType;
}

export interface AnswerQuestionResponse extends QuestionBaseResponse {
  type: typeof QUESTION_TYPE.shortAnswer | typeof QUESTION_TYPE.longAnswer;
}

export interface ChoiceQuestionResponse extends QuestionBaseResponse {
  type:
    | typeof QUESTION_TYPE.singleChoice
    | typeof QUESTION_TYPE.multipleChoice
    | typeof QUESTION_TYPE.trueOrFalse;
  options: QuestionOptionResponse[];
}

export interface QuestionOptionResponse {
  id: string;
  text: string;
  sequence: number;
}

export interface QuestionBaseRequest {
  text: string;
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
  options: string[];
  correctAnswerNumber: number;
}

export interface MultipleChoiceQuestionRequest extends QuestionBaseRequest {
  type: typeof QUESTION_TYPE.multipleChoice;
  options: string[];
  correctAnswerNumbers: number[];
}

export interface TrueOrFalseQuestionRequest extends QuestionBaseRequest {
  type: typeof QUESTION_TYPE.trueOrFalse;
  trueText: string;
  falseText: string;
  trueOrFalse: boolean;
}
