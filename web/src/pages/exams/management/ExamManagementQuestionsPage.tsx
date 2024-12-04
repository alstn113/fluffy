import { useParams } from 'react-router';
import useGetExamWithAnswers from '@/hooks/api/exam/useGetExamWithAnswers.ts';
import AsyncBoundary from '@/components/AsyncBoundary.tsx';
import ExamManagementQuestionsSidebar from '@/components/questions/ExamManagementQuestionsSidebar';
import ExamManagementQuestionsPanel from '@/components/questions/ExamManagementQuestionsPanel';

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

const ExamManagementQuestionsContent = ({ examId }: { examId: number }) => {
  const { data } = useGetExamWithAnswers(examId);
  const isPublished = data.status === 'PUBLISHED';

  return (
    <>
      <ExamManagementQuestionsSidebar isPublished={isPublished} />
      <ExamManagementQuestionsPanel isPublished={isPublished} examId={examId} />
    </>
  );
};

export default ExamManagementQuestionsPage;
