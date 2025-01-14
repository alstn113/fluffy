import QuestionEditorTemplate from '@/components/questions/editor/QuestionEditorTemplate.tsx';
import QuestionTypeSelector from '@/components/questions/editor/QuestionTypeSelector.tsx';
import { Button } from '@nextui-org/react';
import useExamEditorStore from '@/stores/useExamEditorStore.ts';
import useUpdateExamQuestions from '@/hooks/api/exam/useUpdateExamQuestions.ts';
import useGetExamWithAnswers from '@/hooks/api/exam/useGetExamWithAnswers';
import QuestionViewTemplate from './view/QuestionViewTemplate';
import AsyncBoundary from '../AsyncBoundary';

interface ExamManagementQuestionsPanelProps {
  isPublished: boolean;
  examId: number;
}

const ExamManagementQuestionsPanel = ({
  isPublished,
  examId,
}: ExamManagementQuestionsPanelProps) => {
  return (
    <div className="flex grow flex-col h-full p-6 overflow-y-auto items-center">
      <div className="flex w-full max-w-[650px] flex-col gap-4">
        <AsyncBoundary>
          {isPublished ? <ExamViewPanel examId={examId} /> : <ExamEditorPanel examId={examId} />}
        </AsyncBoundary>
      </div>
    </div>
  );
};

const ExamEditorPanel = ({ examId }: { examId: number }) => {
  const { data } = useGetExamWithAnswers(examId);
  const { questionTypeSelectorActive, questions } = useExamEditorStore();
  const { mutate, isPending } = useUpdateExamQuestions();

  const handleUpdateQuestions = () => {
    mutate({
      examId,
      request: {
        questions,
      },
    });
  };

  return (
    <>
      <h1 className="text-2xl font-bold text-gray-800 mb-4 flex items-center justify-between">
        {data?.title}
        <Button
          color="success"
          variant="shadow"
          onPress={handleUpdateQuestions}
          className="text-white"
          isLoading={isPending}
        >
          임시 저장
        </Button>
      </h1>
      {questionTypeSelectorActive ? <QuestionTypeSelector /> : <QuestionEditorTemplate />}
    </>
  );
};

const ExamViewPanel = ({ examId }: { examId: number }) => {
  const { data } = useGetExamWithAnswers(examId);
  const { questions } = useExamEditorStore();

  /**
   * store에 저장된느 시점이 늦어서 data가 없을 수 있음
   */
  if (questions.length === 0) return;

  return (
    <>
      <h1 className="text-2xl font-bold text-gray-800 mb-4 flex items-center justify-between">
        {data?.title}
      </h1>
      <QuestionViewTemplate />
    </>
  );
};

export default ExamManagementQuestionsPanel;
