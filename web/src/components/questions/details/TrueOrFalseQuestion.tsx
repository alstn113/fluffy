import { useState } from 'react';
import { ChoiceQuestionResponse } from '@/api/questionAPI.ts';

interface TrueOrFalseQuestionProps {
  question: ChoiceQuestionResponse;
}

const TrueOrFalseQuestion = ({ question }: TrueOrFalseQuestionProps) => {
  const [selected, setSelected] = useState<string | null>(null);

  const handleSelect = (value: string) => {
    setSelected(value);
  };

  return (
    <div className="flex flex-col gap-8 p-6 rounded-3xl bg-gray-100 border border-gray-300 shadow-lg transition-shadow duration-200 hover:shadow-xl">
      <div className="flex justify-center">
        <button
          className={`w-36 h-36 mx-4 rounded-full text-white font-bold transition-all duration-300 ${
            selected === 'true'
              ? 'bg-blue-600 transform scale-105'
              : 'bg-blue-400 hover:bg-blue-500'
          }`}
          onClick={() => handleSelect('true')}
        >
          참
        </button>
        <button
          className={`w-36 h-36 mx-4 rounded-full text-white font-bold transition-all duration-300 ${
            selected === 'false' ? 'bg-red-600 transform scale-105' : 'bg-red-400 hover:bg-red-500'
          }`}
          onClick={() => handleSelect('false')}
        >
          거짓
        </button>
      </div>
    </div>
  );
};

export default TrueOrFalseQuestion;
