import { ChoiceQuestionResponse, QuestionBaseResponse, QuestionType } from '@/api/questionAPI';
import ShortAnswerQuestion from './ShortAnswerQuestion';
import TrueOrFalseQuestion from './TrueOrFalseQuestion';
import MultipleChoiceQuestion from './MultipleChoiceQuestion';
import SingleChoiceQuestion from './SingleChoiceQuestion';
import LongAnswerQuestion from './LongAnswerQuestion';

interface QuestionDetailTemplateProps {
  question: QuestionBaseResponse;
  index: number;
}

const QuestionDetailTemplate = ({ question, index }: QuestionDetailTemplateProps) => {
  const detailMap: Record<QuestionType, React.ReactNode> = {
    SHORT_ANSWER: <ShortAnswerQuestion index={index} />,
    LONG_ANSWER: <LongAnswerQuestion index={index} />,
    TRUE_OR_FALSE: <TrueOrFalseQuestion index={index} />,
    SINGLE_CHOICE: (
      <SingleChoiceQuestion question={question as ChoiceQuestionResponse} index={index} />
    ),
    MULTIPLE_CHOICE: (
      <MultipleChoiceQuestion question={question as ChoiceQuestionResponse} index={index} />
    ),
  };

  return (
    <div className="p-4 bg-gray-50 border border-gray-300 rounded-lg mx-auto w-full max-w-md">
      <div className="text-md font-semibold mb-2 text-gray-800">
        {index + 1}. {question.text}
      </div>
      <div className="pt-2">{detailMap[question.type]}</div>
    </div>
  );
};

export default QuestionDetailTemplate;
