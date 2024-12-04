import { useParams } from 'react-router';

import ExamEditorSidebar from '@/components/questions/editors/ExamEditorSidebar.tsx';
import QuestionEditorTemplate from '@/components/questions/editors/QuestionEditorTemplate.tsx';
import QuestionTypeSelector from '@/components/questions/editors/QuestionTypeSelector.tsx';
import useExamEditorStore from '@/stores/useExamEditorStore.ts';
import useUpdateExamQuestions from '@/hooks/api/exam/useUpdateExamQuestions.ts';
import useGetExamWithAnswers from '@/hooks/api/exam/useGetExamWithAnswers.ts';
import AsyncBoundary from '@/components/AsyncBoundary.tsx';
import { Button } from '@nextui-org/react';

const ExamManagementQuestionsPage = () => {
  const params = useParams() as { examId: string };
  const examId = Number(params.examId);

  return (
    <div className="flex flex-row overflow-y-auto h-full w-full">
      <AsyncBoundary>
        <ExamManagementQuestionsContent examId={examId} />
      </AsyncBoundary>
    </div>
  );
};

interface ExamEditContentProps {
  examId: number;
}

const ExamManagementQuestionsContent = ({ examId }: ExamEditContentProps) => {
  const { data } = useGetExamWithAnswers(examId);
  const { questionTypeSelectorActive, questions } = useExamEditorStore();

  const { mutate } = useUpdateExamQuestions();

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
      <ExamEditorSidebar />
      <div className="flex grow flex-col h-full p-6 overflow-y-auto items-center">
        <div className="flex w-full max-w-[650px] flex-col gap-4">
          <h1 className="text-2xl font-bold text-gray-800 mb-4 flex items-center justify-between">
            {data?.title}
            <Button
              color="success"
              variant="shadow"
              onPress={handleUpdateQuestions}
              className="text-white"
            >
              임시 저장
            </Button>
          </h1>
          {questionTypeSelectorActive ? <QuestionTypeSelector /> : <QuestionEditorTemplate />}
        </div>
      </div>
    </>
  );
};

export default ExamManagementQuestionsPage;
