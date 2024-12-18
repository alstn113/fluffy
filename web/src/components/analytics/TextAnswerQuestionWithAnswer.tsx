import { TextQuestionType } from '@/api/questionAPI';
import { TextAnswerResponse } from '@/api/submissionAPI';
import { Chip } from '@nextui-org/react';

interface TextAnswerQuestionWithAnswerProps {
  answer: TextAnswerResponse;
  textQuestionType: TextQuestionType;
}

const TextAnswerQuestionWithAnswer = ({
  answer,
  textQuestionType,
}: TextAnswerQuestionWithAnswerProps) => {
  return (
    <div className="p-6 rounded-2xl bg-white border border-gray-200">
      <div className="flex flex-col gap-2">
        <div className="flex items-center gap-2">
          <Chip color="secondary">답안</Chip>
          <div>{answer.answer}</div>
        </div>
        {textQuestionType === 'SHORT_ANSWER' && (
          <div className="flex items-center gap-2">
            <Chip
              color="success"
              classNames={{
                content: 'text-white',
              }}
            >
              정답
            </Chip>
            <div>{answer.correctAnswer}</div>
          </div>
        )}
      </div>
    </div>
  );
};

export default TextAnswerQuestionWithAnswer;
