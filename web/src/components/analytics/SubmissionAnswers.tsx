import TextAnswerQuestionWithAnswer from './TextAnswerQuestionWithAnswer';
import TrueOrFalseQuestionWithAnswer from './TrueOrFalseQuestionWithAnswer';
import SingleChoiceQuestionWithAnswer from './SingleChoiceQuestionWithAnswer';
import MultipleChoiceQuestionWithAnswer from './MultipleChoiceQuestionWithAnswer';
import { AnswerBaseResponse, ChoiceAnswerResponse, TextAnswerResponse } from '@/api/submissionAPI';

interface SubmissionAnswersProps {
  answer: AnswerBaseResponse;
  index: number;
}

const SubmissionAnswers = ({ answer, index }: SubmissionAnswersProps) => {
  const isCorrect = (answer: AnswerBaseResponse) => {
    if (answer.type === 'SHORT_ANSWER') {
      const shortAnswerQuestionAnswer = answer as TextAnswerResponse;
      return shortAnswerQuestionAnswer.answer === shortAnswerQuestionAnswer.correctAnswer;
    }
    if (answer.type === 'LONG_ANSWER') {
      return null;
    }
    if (answer.type === 'TRUE_OR_FALSE') {
      const trueOrFalseQuestionAnswer = answer as ChoiceAnswerResponse;
      return (
        trueOrFalseQuestionAnswer.choices[0].isCorrect ===
        trueOrFalseQuestionAnswer.choices[0].isSelected
      );
    }
    if (answer.type === 'SINGLE_CHOICE') {
      const singleChoiceQuestionAnswer = answer as ChoiceAnswerResponse;
      return singleChoiceQuestionAnswer.choices.every(
        (choice) => choice.isCorrect === choice.isSelected,
      );
    }
    if (answer.type === 'MULTIPLE_CHOICE') {
      const multipleChoiceQuestionAnswer = answer as ChoiceAnswerResponse;
      return multipleChoiceQuestionAnswer.choices.every(
        (choice) => choice.isCorrect === choice.isSelected,
      );
    }
  };

  const answerMap = (answer: AnswerBaseResponse) => ({
    SHORT_ANSWER: (
      <TextAnswerQuestionWithAnswer
        answer={answer as TextAnswerResponse}
        textQuestionType="SHORT_ANSWER"
      />
    ),
    LONG_ANSWER: (
      <TextAnswerQuestionWithAnswer
        answer={answer as TextAnswerResponse}
        textQuestionType="LONG_ANSWER"
      />
    ),
    TRUE_OR_FALSE: <TrueOrFalseQuestionWithAnswer answer={answer as ChoiceAnswerResponse} />,
    SINGLE_CHOICE: <SingleChoiceQuestionWithAnswer answer={answer as ChoiceAnswerResponse} />,
    MULTIPLE_CHOICE: <MultipleChoiceQuestionWithAnswer answer={answer as ChoiceAnswerResponse} />,
  });

  return (
    <div
      className={`p-4 bg-gray-50 border-l-4 rounded-sm w-full
        ${
          isCorrect(answer) === true
            ? 'border-green-400'
            : isCorrect(answer) === false
              ? 'border-red-400'
              : 'border-gray-400'
        }`}
    >
      <div className="text-md font-semibold mb-2 text-gray-800">
        {index + 1}. {answer.text}
      </div>
      <div className="pt-2">{answerMap(answer)[answer.type]}</div>
    </div>
  );
};

export default SubmissionAnswers;
