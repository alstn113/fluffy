import { ChoiceAnswerResponse } from '@/api/submissionAPI';
import { Chip } from '@heroui/react';

interface TrueOrFalseQuestionWithAnswerProps {
  answer: ChoiceAnswerResponse;
}

const TrueOrFalseQuestionWithAnswer = ({ answer }: TrueOrFalseQuestionWithAnswerProps) => {
  return (
    <div className="p-6 rounded-2xl bg-white border border-gray-200">
      <div className="flex flex-col gap-2">
        <div className="flex items-center gap-2">
          <Chip color="secondary">답안</Chip>
          <div>{answer.choices[0].isCorrect ? 'TRUE' : 'FALSE'}</div>
        </div>
        <div className="flex items-center gap-2">
          <Chip
            color="success"
            classNames={{
              content: 'text-white',
            }}
          >
            정답
          </Chip>
          <>{answer.choices[0].isSelected ? 'TRUE' : 'FALSE'}</>
        </div>
      </div>
    </div>
  );
};

export default TrueOrFalseQuestionWithAnswer;
