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
    <div className="w-full mx-auto">
      <div className="w-full text-md font-semibold text-gray-800">
        {index + 1}. {question.text}
      </div>
      <div className="w-full mt-4">{detailMap[question.type]}</div>
    </div>
  );
};

export default QuestionDetailTemplate;
