import { ChoiceAnswerResponse } from '@/api/submissionAPI';
import { Checkbox } from '@nextui-org/react';

interface MultipleChoiceQuestionWithAnswerProps {
  answer: ChoiceAnswerResponse;
}

const MultipleChoiceQuestionWithAnswer = ({ answer }: MultipleChoiceQuestionWithAnswerProps) => {
  const toColor = (isSelected: boolean, isCorrect: boolean) => {
    if (isSelected && isCorrect) return 'bg-green-50 border-green-300';
    if (!isSelected && isCorrect) return 'bg-yellow-50 border-yellow-300';
    if (isSelected && !isCorrect) return 'bg-red-50 border-red-300';
    return 'bg-transparent border-gray-300';
  };

  return (
    <div className="p-6 rounded-2xl bg-white border border-gray-200">
      <label className="mb-2 font-semibold">선택지와 정답</label>
      <div>
        {answer.choices.map((choice, index) => (
          <div key={index} className="flex items-center p-2 rounded-md">
            <Checkbox color="secondary" isSelected={choice.isSelected} disableAnimation />
            <input
              readOnly
              type="text"
              value={choice.text}
              className={`p-2 border-b rounded-none ml-2 focus:outline-none min-w-[300px]
                  ${toColor(choice.isSelected, choice.isCorrect)}
                `}
            />
          </div>
        ))}
      </div>
    </div>
  );
};

export default MultipleChoiceQuestionWithAnswer;
