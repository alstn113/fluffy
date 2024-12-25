import useSubmissionStore from '@/stores/useSubmissionStore';
import { GoThumbsup, GoThumbsdown } from 'react-icons/go';

interface TrueOrFalseQuestionProps {
  index: number;
}

const TrueOrFalseQuestion = ({ index }: TrueOrFalseQuestionProps) => {
  const { questionResponses, handleUpdateChoiceAnswer } = useSubmissionStore();

  const choice = questionResponses[index]?.answers[0] || '';
  const handleUpdate = (selected: string) => {
    handleUpdateChoiceAnswer(index, [selected]);
  };

  const ANSWERS = {
    true: 'TRUE',
    false: 'FALSE',
  } as const;

  return (
    <div className="flex w-full flex-col mt-6">
      <div className="flex gap-2">
        <AnswerButton
          selected={choice === ANSWERS.true}
          onClick={() => handleUpdate(ANSWERS.true)}
          icon={<GoThumbsup />}
          label="True"
        />
        <AnswerButton
          selected={choice === ANSWERS.false}
          onClick={() => handleUpdate(ANSWERS.false)}
          icon={<GoThumbsdown />}
          label="False"
        />
      </div>
    </div>
  );
};

interface AnswerButtonProps {
  selected: boolean;
  onClick: () => void;
  icon: React.ReactNode;
  label: string;
}

const AnswerButton = ({ selected, onClick, icon, label }: AnswerButtonProps) => {
  const baseClass =
    'flex flex-1 flex-col justify-center items-center p-4 gap-2 border-2 rounded-lg transition-all duration-200';
  const selectedClass = selected
    ? 'bg-white border-purple-300 text-black'
    : 'bg-white border-gray-300';
  const hoverClass = 'hover:bg-gray-50';

  return (
    <button className={`${baseClass} ${selectedClass} ${hoverClass}`} onClick={onClick}>
      <div>{icon}</div>
      <div className="text-sm">{label}</div>
    </button>
  );
};

export default TrueOrFalseQuestion;
