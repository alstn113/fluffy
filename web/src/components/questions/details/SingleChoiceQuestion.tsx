import { useState } from 'react';
import { ChoiceQuestionResponse } from '@/api/questionAPI.ts';
import { Radio, RadioGroup } from '@nextui-org/radio';

interface SingleChoiceQuestionProps {
  question: ChoiceQuestionResponse;
}

const SingleChoiceQuestion = ({ question }: SingleChoiceQuestionProps) => {
  const { options } = question;
  const [selected, setSelected] = useState<string | null>(null);

  return (
    <div className="flex flex-col gap-4 p-6 rounded-2xl bg-white border border-gray-200 shadow-md duration-200 hover:shadow-lg">
      <RadioGroup value={selected} onValueChange={setSelected}>
        {options.map((option, index) => (
          <Radio color="secondary" key={option.id} value={option.id} className="text-gray-800">
            {index + 1}. {option.text}
          </Radio>
        ))}
      </RadioGroup>
    </div>
  );
};

export default SingleChoiceQuestion;
