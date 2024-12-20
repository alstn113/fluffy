import useSubmissionStore from '@/stores/useSubmissionStore';

interface LongAnswerQuestionProps {
  index: number;
}

const LongAnswerQuestion = ({ index }: LongAnswerQuestionProps) => {
  const { questionResponses, handleUpdateTextAnswer } = useSubmissionStore();
  const text = questionResponses[index]?.answers[0] || '';

  return (
    <textarea
      value={text}
      onChange={(e) => handleUpdateTextAnswer(index, e.target.value)}
      placeholder="답변을 입력하세요..."
      className="w-full flex-1 p-4 border border-gray-300 rounded-lg text-gray-800 text-base resize-none transition-colors duration-200 focus:border-green-500 focus:ring-1 focus:ring-green-300 focus:outline-none"
      rows={4}
    />
  );
};

export default LongAnswerQuestion;
