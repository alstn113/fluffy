import BaseLayout from '@/components/layouts/base/BaseLayout.tsx';
import useGetExamSummaries from '@/hooks/api/exam/useGetExamSummaries';
import AsyncBoundary from '@/components/AsyncBoundary';
import ExamSummaryCard from '@/components/exams/ExamSummaryCard';

const ExamListPage = () => {
  return (
    <BaseLayout>
      <AsyncBoundary>
        <div className="container mx-auto px-5 py-16">
          <div className="text-2xl font-semibold mb-5">Recent Exams</div>
          <ExamListContent />
        </div>
      </AsyncBoundary>
    </BaseLayout>
  );
};

const ExamListContent = () => {
  const { data } = useGetExamSummaries();

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
      {data?.map((exam) => {
        return <ExamSummaryCard exam={exam} />;
      })}
    </div>
  );
};

export default ExamListPage;
