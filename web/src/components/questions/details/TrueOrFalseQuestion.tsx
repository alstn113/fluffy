import { useState } from 'react';
import { ChoiceQuestionResponse } from '@/api/questionAPI.ts';

interface TrueOrFalseQuestionProps {
  question: ChoiceQuestionResponse;
}

const TrueOrFalseQuestion = ({ question }: TrueOrFalseQuestionProps) => {
  console.log(question);

  const [selected, setSelected] = useState<string | null>(null);

  const handleSelect = (value: string) => {
    setSelected(value);
  };

  return (
    <div className="flex flex-col gap-8 p-6 rounded-3xl bg-gray-100 border border-gray-300 shadow-lg transition-shadow duration-200 hover:shadow-xl">
      <div className="flex justify-center">
        <button
          className={`md:w-32 md:h-32 w-16 h-16 mx-4 rounded-full text-white font-bold transition-all duration-300 ${
            selected === 'true'
              ? 'bg-blue-600 transform scale-110'
              : 'bg-blue-300 hover:bg-blue-500'
          }`}
          onClick={() => handleSelect('true')}
        >
          True
        </button>
        <button
          className={`md:w-32 md:h-32 w-16 h-16 mx-4 rounded-full text-white font-bold transition-all duration-300 ${
            selected === 'false' ? 'bg-red-600 transform scale-110' : 'bg-red-300 hover:bg-red-500'
          }`}
          onClick={() => handleSelect('false')}
        >
          False
        </button>
      </div>
    </div>
  );
};

export default TrueOrFalseQuestion;
