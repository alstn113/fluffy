import ExamManagementHeader from './ExamManagementHeader.tsx';
import { Outlet, useParams } from 'react-router';
import useGetExamWithAnswers from '@/hooks/api/exam/useGetExamWithAnswers.ts';
import useExamEditorStore from '@/stores/useExamEditorStore.ts';
import { useEffect } from 'react';
import AsyncBoundary from '@/components/AsyncBoundary.tsx';

const ExamManagementLayout = () => {
  const params = useParams() as { examId: string };
  const examId = Number(params.examId);

  return (
    <div className="flex flex-col h-screen w-screen bg-white">
      <AsyncBoundary>
        <ExamManagementLayoutContent examId={examId} />
      </AsyncBoundary>
    </div>
  );
};

const ExamManagementLayoutContent = ({ examId }: { examId: number }) => {
  const { data } = useGetExamWithAnswers(examId);
  const { initialize } = useExamEditorStore();

  useEffect(() => {
    if (data && data.questions) {
      initialize(data.questions);
    }
  }, [data, initialize]);

  return (
    <>
      <ExamManagementHeader />
      <div className="h-full w-full overflow-y-auto pb-14 pt-6 bg-gray-100">
        <main className="h-full w-full overflow-y-auto bg-white">
          <Outlet />
        </main>
      </div>
    </>
  );
};

export default ExamManagementLayout;
