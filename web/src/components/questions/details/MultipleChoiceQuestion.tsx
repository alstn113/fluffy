import { useState } from 'react';
import { ChoiceQuestionResponse } from '@/api/questionAPI.ts';
import { Checkbox, CheckboxGroup } from '@nextui-org/checkbox';

interface MultipleChoiceQuestionProps {
  question: ChoiceQuestionResponse;
}

const MultipleChoiceQuestion = ({ question }: MultipleChoiceQuestionProps) => {
  const { options } = question;
  const [selected, setSelected] = useState<string[]>([]);

  return (
    <div className="flex flex-col gap-4 p-6 rounded-2xl bg-white border border-gray-200 shadow-md duration-200 hover:shadow-lg">
      <CheckboxGroup color="secondary" value={selected} onValueChange={setSelected}>
        {options.map((option, index) => (
          <Checkbox key={option.id} value={option.id} className="text-gray-800">
            <span className="text-base">
              {index + 1}. {option.text}
            </span>
          </Checkbox>
        ))}
      </CheckboxGroup>
    </div>
  );
};

export default MultipleChoiceQuestion;
