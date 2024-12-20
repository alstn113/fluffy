import { ChoiceQuestionResponse } from '@/api/questionAPI.ts';
import { Radio, RadioGroup } from '@nextui-org/radio';
import useSubmissionStore from '@/stores/useSubmissionStore';

interface SingleChoiceQuestionProps {
  question: ChoiceQuestionResponse;
  index: number;
}

const SingleChoiceQuestion = ({ question, index }: SingleChoiceQuestionProps) => {
  const { questionResponses, handleUpdateChoiceAnswer } = useSubmissionStore();
  const { options } = question;

  const choice = questionResponses[index]?.answers[0] || '';
  const handleUpdate = (selected: string) => {
    handleUpdateChoiceAnswer(index, [selected]);
  };

  return (
    <RadioGroup value={choice} onValueChange={handleUpdate}>
      {options.map((option, index) => (
        <Radio color="secondary" key={option.text} value={option.text} className="text-gray-800">
          {index + 1}. {option.text}
        </Radio>
      ))}
    </RadioGroup>
  );
};

export default SingleChoiceQuestion;
