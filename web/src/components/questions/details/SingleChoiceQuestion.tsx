import { ChoiceQuestionResponse } from '@/api/questionAPI.ts';
import { Radio, RadioGroup } from '@nextui-org/radio';
import useSubmissionStore from '@/stores/useSubmissionStore';

interface SingleChoiceQuestionProps {
  question: ChoiceQuestionResponse;
  index: number;
}

const SingleChoiceQuestion = ({ question, index }: SingleChoiceQuestionProps) => {
  const { answers, handleUpdateChoices } = useSubmissionStore();
  const { options } = question;

  const choice = answers[index]?.choices.map((choice) => String(choice))[0] || null;
  const handleUpdate = (selected: string) => {
    handleUpdateChoices(index, [Number(selected)]);
  };

  return (
    <div className="flex flex-col gap-4 p-6 rounded-2xl bg-white border border-gray-200 shadow-md duration-200 hover:shadow-lg">
      <RadioGroup value={choice} onValueChange={handleUpdate}>
        {options.map((option, index) => (
          <Radio color="secondary" key={index} value={String(index + 1)} className="text-gray-800">
            {index + 1}. {option.text}
          </Radio>
        ))}
      </RadioGroup>
    </div>
  );
};

export default SingleChoiceQuestion;
