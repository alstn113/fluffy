import { useParams } from 'react-router-dom';
import ExamEditorSidebar from '@/components/questions/editors/ExamEditorSidebar.tsx';
import QuestionEditorTemplate from '@/components/questions/editors/QuestionEditorTemplate';
import QuestionTypeSelector from '@/components/questions/editors/QuestionTypeSelector';
import useExamEditorStore from '@/stores/useExamEditorStore';
import useUpdateExamQuestions from '@/hooks/api/exam/useUpdateExamQuestions';
import EditorLayout from '@/components/layouts/editor/EditorLayout';
import useGetExamWithAnswers from '@/hooks/api/exam/useGetExamWithAnswers';
import AsyncBoundary from '@/components/AsyncBoundary';
import { useEffect } from 'react';

const ExamEditPage = () => {
  const { id } = useParams() as { id: string };
  const examId = Number(id);

  return (
    <EditorLayout>
      <div className="flex flex-row overflow-y-auto h-full w-full">
        <AsyncBoundary>
          <ExamEditContent examId={examId} />
        </AsyncBoundary>
      </div>
    </EditorLayout>
  );
};

interface ExamEditContentProps {
  examId: number;
}

const ExamEditContent = ({ examId }: ExamEditContentProps) => {
  const { data } = useGetExamWithAnswers(examId);
  const { questionTypeSelectorActive, questions, initialize } = useExamEditorStore();

  useEffect(() => {
    if (data && data.questions) {
      initialize(data.questions);
    }
  }, [data, initialize]);

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
            시험 ({examId})
            <button
              onClick={handleUpdateQuestions}
              className="ml-2 px-2 py-1 text-medium bg-green-600 text-white rounded-md w-20 hover:bg-green-700 transition duration-200 shadow-md"
            >
              임시 저장
            </button>
          </h1>
          {questionTypeSelectorActive ? <QuestionTypeSelector /> : <QuestionEditorTemplate />}
        </div>
      </div>
    </>
  );
};

export default ExamEditPage;
