import useExamEditorStore from '@/stores/useExamEditorStore';
import { MultipleChoiceQuestionRequest } from '@/api/questionAPI';
import { Checkbox } from '@nextui-org/react';

const MultipleChoiceQuestionView = () => {
  const { currentIndex, questions } = useExamEditorStore();
  const question = questions[currentIndex] as MultipleChoiceQuestionRequest;

  return (
    <div className="flex flex-col mt-8">
      <label className="mb-2 font-semibold">선택지와 정답</label>
      <div>
        {question.options.map((option, index) => (
          <div key={index} className="flex items-center p-2 rounded-md mb-2 hover:bg-gray-100">
            <Checkbox color="secondary" isSelected={option.isCorrect} disableAnimation />
            <input
              readOnly
              type="text"
              value={option.text}
              className="p-2 border-b border-gray-300 rounded-none ml-2 bg-transparent focus:outline-none min-w-[300px]"
            />
          </div>
        ))}
      </div>
    </div>
  );
};

export default MultipleChoiceQuestionView;
