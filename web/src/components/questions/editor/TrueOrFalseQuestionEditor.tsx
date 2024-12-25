import useExamEditorStore from '@/stores/useExamEditorStore';
import { TrueOrFalseQuestionRequest } from '@/api/questionAPI';
import { GoThumbsup, GoThumbsdown } from 'react-icons/go';

const TrueOrFalseQuestionEditor = () => {
  const { handleUpdateQuestion, currentIndex, questions } = useExamEditorStore();
  const question = questions[currentIndex] as TrueOrFalseQuestionRequest;

  const handleUpdateCorrectAnswer = (isTrue: boolean) => {
    const newQuestion: TrueOrFalseQuestionRequest = {
      ...question,
      trueOrFalse: isTrue,
    };
    handleUpdateQuestion(currentIndex, newQuestion);
  };

  return (
    <div className="flex flex-col mt-8">
      <label className="mb-2 font-semibold">정답</label>
      <div className="flex w-full flex-col">
        <div className="flex gap-2">
          <AnswerButton
            selected={question.trueOrFalse === true}
            onClick={() => handleUpdateCorrectAnswer(true)}
            icon={<GoThumbsup />}
            label="True"
          />
          <AnswerButton
            selected={question.trueOrFalse === false}
            onClick={() => handleUpdateCorrectAnswer(false)}
            icon={<GoThumbsdown />}
            label="False"
          />
        </div>
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

export default TrueOrFalseQuestionEditor;
