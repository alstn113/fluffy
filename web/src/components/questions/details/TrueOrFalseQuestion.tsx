import useSubmissionStore from '@/stores/useSubmissionStore';

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
    <div className="flex justify-center mt-6">
      <button
        className={`md:w-32 md:h-32 w-24 h-24 mx-4 rounded-full text-white font-bold transition-all duration-300 ${
          choice === ANSWERS.true
            ? 'bg-blue-600 transform scale-110'
            : 'bg-blue-300 hover:bg-blue-500'
        }`}
        onClick={() => handleUpdate(ANSWERS.true)}
      >
        True
      </button>
      <button
        className={`md:w-32 md:h-32 w-24 h-24 mx-4 rounded-full text-white font-bold transition-all duration-300 ${
          choice === ANSWERS.false
            ? 'bg-red-600 transform scale-110'
            : 'bg-red-300 hover:bg-red-500'
        }`}
        onClick={() => handleUpdate(ANSWERS.false)}
      >
        False
      </button>
    </div>
  );
};

export default TrueOrFalseQuestion;
