import { ChoiceAnswerResponse } from '@/api/submissionAPI';
import { Radio, RadioGroup } from '@nextui-org/react';

interface SingleChoiceQuestionWithAnswerProps {
  answer: ChoiceAnswerResponse;
}

const SingleChoiceQuestionWithAnswer = ({ answer }: SingleChoiceQuestionWithAnswerProps) => {
  const toColor = (isSelected: boolean, isCorrect: boolean) => {
    if (isSelected && isCorrect) return 'bg-green-50 border-green-300';
    if (!isSelected && isCorrect) return 'bg-yellow-50 border-yellow-300';
    if (isSelected && !isCorrect) return 'bg-red-50 border-red-300';
    return 'bg-transparent border-gray-300';
  };

  return (
    <div className="p-6 rounded-2xl bg-white border border-gray-200">
      <label className="mb-2 font-semibold">선택지와 정답</label>
      <RadioGroup value={answer.choices.findIndex((option) => option.isSelected).toString()}>
        <div>
          {answer.choices.map((choice, index) => (
            <div key={index} className="flex items-center p-2 rounded-md">
              <Radio color="secondary" value={index.toString()} disableAnimation />
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
      </RadioGroup>
    </div>
  );
};

export default SingleChoiceQuestionWithAnswer;
