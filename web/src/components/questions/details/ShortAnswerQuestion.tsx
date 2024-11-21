import useSubmissionStore from '@/stores/useSubmissionStore';

interface ShortAnswerQuestionProps {
  index: number;
}

const ShortAnswerQuestion = ({ index }: ShortAnswerQuestionProps) => {
  const { handleUpdateText, answers } = useSubmissionStore();

  const text = answers[index]?.text || '';

  return (
    <div className="flex flex-col gap-4 p-6 rounded-2xl bg-white border border-gray-200 shadow-md duration-200 hover:shadow-lg">
      <input
        value={text}
        onChange={(e) => handleUpdateText(index, e.target.value)}
        placeholder="답변을 입력하세요..."
        className="flex-1 p-4 border border-gray-300 rounded-lg text-gray-800 text-base resize-none transition-colors duration-200 focus:border-green-500 focus:ring-1 focus:ring-green-300 focus:outline-none"
      />
    </div>
  );
};

export default ShortAnswerQuestion;
