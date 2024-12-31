import ExamManagementAnalyticsContent from '@/components/analytics/ExamManagementAnalyticsContent';
import AsyncBoundary from '@/components/AsyncBoundary.tsx';
import { useParams } from 'react-router';

const ExamManagementAnalyticsPage = () => {
  const params = useParams() as { examId: string };
  const examId = Number(params.examId);

  return (
    <div className="w-full h-full flex items-center flex-col gap-4">
      <div className="max-w-[750px] w-full mt-8">
        <AsyncBoundary>
          <ExamManagementAnalyticsContent examId={examId} />
        </AsyncBoundary>
      </div>
    </div>
  );
};

export default ExamManagementAnalyticsPage;
