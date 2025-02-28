import { QuestionType } from '@/api/questionAPI';
import useExamEditorStore from '@/stores/useExamEditorStore';
import { Input } from '@heroui/react';
import ShortAnswerQuestionView from './ShortAnswerQuestionView';
import LongAnswerQuestionView from './LongAnswerQuestionView';
import SingleChoiceQuestionView from './SingleChoiceQuestionView';
import MultipleChoiceQuestionView from './MultipleChoiceQuestionView';
import TrueOrFalseQuestionView from './TrueOrFalseQuestionView';
import MDEditor from '@uiw/react-md-editor';

const QuestionViewTemplate = () => {
  const { currentIndex, questions } = useExamEditorStore();
  const question = questions[currentIndex];

  const viewMap: Record<QuestionType, React.ReactNode> = {
    SHORT_ANSWER: <ShortAnswerQuestionView />,
    LONG_ANSWER: <LongAnswerQuestionView />,
    SINGLE_CHOICE: <SingleChoiceQuestionView />,
    MULTIPLE_CHOICE: <MultipleChoiceQuestionView />,
    TRUE_OR_FALSE: <TrueOrFalseQuestionView />,
  };

  return (
    <div>
      <div className="flex gap-2 justify-between items-center mb-4">
        <div className=" text-2xl font-bold mb-2">{currentIndex + 1}번째 질문</div>
      </div>
      <Input
        isReadOnly
        value={question.text}
        label="질문"
        variant="underlined"
        className="w-full mb-2"
        classNames={{
          label: 'text-xl font-semibold text-gray-800',
        }}
      />
      {question.passage && (
        <MDEditor.Markdown
          className="mt-4"
          source={question.passage}
          style={{ whiteSpace: 'pre-wrap' }}
        />
      )}
      {viewMap[question.type]}
    </div>
  );
};

export default QuestionViewTemplate;
