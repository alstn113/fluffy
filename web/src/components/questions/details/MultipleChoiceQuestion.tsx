import { ChoiceQuestionResponse } from '@/api/questionAPI.ts';
import { Checkbox, CheckboxGroup } from '@nextui-org/checkbox';
import useSubmissionStore from '@/stores/useSubmissionStore';

interface MultipleChoiceQuestionProps {
  question: ChoiceQuestionResponse;
  index: number;
}

const MultipleChoiceQuestion = ({ question, index }: MultipleChoiceQuestionProps) => {
  const { questionResponses, handleUpdateChoiceAnswer } = useSubmissionStore();
  const { options } = question;

  const choices = questionResponses[index]?.answers || [];
  const handleUpdate = (selected: string[]) => {
    handleUpdateChoiceAnswer(index, selected);
  };

  return (
    <div className="flex flex-col gap-4 p-6 rounded-2xl bg-white border border-gray-200 shadow-md duration-200 hover:shadow-lg">
      <CheckboxGroup color="secondary" value={choices} onValueChange={handleUpdate}>
        {options.map((option, index) => (
          <Checkbox key={option.text} value={option.text} className="text-gray-800">
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
