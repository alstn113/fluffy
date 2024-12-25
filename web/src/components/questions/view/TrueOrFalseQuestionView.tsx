import useExamEditorStore from '@/stores/useExamEditorStore';
import { TrueOrFalseQuestionRequest } from '@/api/questionAPI';
import { GoThumbsup, GoThumbsdown } from 'react-icons/go';

const TrueOrFalseQuestionView = () => {
  const { currentIndex, questions } = useExamEditorStore();
  const question = questions[currentIndex] as TrueOrFalseQuestionRequest;

  return (
    <div className="flex flex-col mt-8">
      <label className="mb-2 font-semibold">정답</label>
      <div className="flex w-full flex-col">
        <div className="flex gap-2">
          <AnswerView isTrue={question.trueOrFalse === true} icon={<GoThumbsup />} label="True" />
          <AnswerView
            isTrue={question.trueOrFalse === false}
            icon={<GoThumbsdown />}
            label="False"
          />
        </div>
      </div>
    </div>
  );
};

interface AnswerButtonProps {
  isTrue: boolean;
  icon: React.ReactNode;
  label: string;
}

const AnswerView = ({ isTrue, icon, label }: AnswerButtonProps) => {
  const baseClass =
    'flex flex-1 flex-col justify-center items-center p-4 gap-2 border-2 rounded-lg';
  const isTrueClass = isTrue ? 'bg-white border-purple-300 text-black' : 'bg-white border-gray-300';

  return (
    <div className={`${baseClass} ${isTrueClass}`}>
      <div>{icon}</div>
      <div className="text-sm">{label}</div>
    </div>
  );
};

export default TrueOrFalseQuestionView;
