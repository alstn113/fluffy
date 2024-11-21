import useSubmissionStore from '@/stores/useSubmissionStore';

interface TrueOrFalseQuestionProps {
  index: number;
}

const TrueOrFalseQuestion = ({ index }: TrueOrFalseQuestionProps) => {
  const { answers, handleUpdateChoices } = useSubmissionStore();

  const choice = answers[index]?.choices.map((choice) => String(choice))[0] || null;
  const handleUpdate = (selected: string) => {
    handleUpdateChoices(index, [Number(selected)]);
  };

  return (
    <div className="flex flex-col gap-8 p-6 rounded-3xl bg-gray-100 border border-gray-300 shadow-lg transition-shadow duration-200 hover:shadow-xl">
      <div className="flex justify-center">
        <button
          className={`md:w-32 md:h-32 w-16 h-16 mx-4 rounded-full text-white font-bold transition-all duration-300 ${
            choice === '1' ? 'bg-blue-600 transform scale-110' : 'bg-blue-300 hover:bg-blue-500'
          }`}
          onClick={() => handleUpdate('1')}
        >
          True
        </button>
        <button
          className={`md:w-32 md:h-32 w-16 h-16 mx-4 rounded-full text-white font-bold transition-all duration-300 ${
            choice === '2' ? 'bg-red-600 transform scale-110' : 'bg-red-300 hover:bg-red-500'
          }`}
          onClick={() => handleUpdate('2')}
        >
          False
        </button>
      </div>
    </div>
  );
};

export default TrueOrFalseQuestion;
