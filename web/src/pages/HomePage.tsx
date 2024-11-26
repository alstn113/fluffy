import BaseLayout from '@/components/layouts/base/BaseLayout.tsx';
import NewExamButton from '@/components/exams/NewExamButton';
import AsyncBoundary from '@/components/AsyncBoundary';
import useGetExamSummaries from '@/hooks/api/exam/useGetExamSummaries';
import ExamSummaryCard from '@/components/exams/ExamSummaryCard';

const HomePage = () => {
  return (
    <BaseLayout>
      <div className="container mx-auto px-5 py-12">
        <div className="w-full flex justify-center items-center">
          <NewExamButton />
        </div>
        <div className="text-2xl font-semibold mb-5">최근 시험 목록</div>
        <AsyncBoundary>
          <ExamListContent />
        </AsyncBoundary>
      </div>
    </BaseLayout>
  );
};

const ExamListContent = () => {
  const { data } = useGetExamSummaries();

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
      {data?.map((exam) => {
        return <ExamSummaryCard key={exam.id} exam={exam} />;
      })}
    </div>
  );
};

export default HomePage;
