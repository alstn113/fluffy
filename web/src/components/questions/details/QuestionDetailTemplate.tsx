import {
  AnswerQuestionResponse,
  ChoiceQuestionResponse,
  QuestionBaseResponse,
  QuestionType,
} from '@/api/questionAPI';
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
    SHORT_ANSWER: <ShortAnswerQuestion question={question as AnswerQuestionResponse} />,
    LONG_ANSWER: <LongAnswerQuestion question={question as AnswerQuestionResponse} />,
    SINGLE_CHOICE: <SingleChoiceQuestion question={question as ChoiceQuestionResponse} />,
    MULTIPLE_CHOICE: <MultipleChoiceQuestion question={question as ChoiceQuestionResponse} />,
    TRUE_OR_FALSE: <TrueOrFalseQuestion question={question as ChoiceQuestionResponse} />,
  };

  return (
    <div className="p-4 bg-gray-50 border border-gray-300 rounded-lg min-w-[600px] mx-auto">
      <div className="text-md font-semibold mb-2 text-gray-800">
        {index + 1}. {question.text}
      </div>
      <div className="pt-2">{detailMap[question.type]}</div>
    </div>
  );
};

export default QuestionDetailTemplate;
