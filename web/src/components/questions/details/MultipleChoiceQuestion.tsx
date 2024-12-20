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
    <CheckboxGroup color="secondary" value={choices} onValueChange={handleUpdate}>
      {options.map((option, index) => (
        <Checkbox key={option.text} value={option.text} className="text-gray-800">
          <span className="text-base">
            {index + 1}. {option.text}
          </span>
        </Checkbox>
      ))}
    </CheckboxGroup>
  );
};

export default MultipleChoiceQuestion;
