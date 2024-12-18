import AsyncBoundary from '@/components/AsyncBoundary';
import useGetExamSummaries from '@/hooks/api/exam/useGetExamSummaries';
import ExamSummaryCard from '@/components/exams/ExamSummaryCard';

const HomePage = () => {
  return (
    <div className="container mx-auto px-5 py-12">
      <div className="text-2xl font-semibold mb-5">최근 시험 목록</div>
      <AsyncBoundary>
        <ExamListContent />
      </AsyncBoundary>
    </div>
  );
};

const ExamListContent = () => {
  const { data } = useGetExamSummaries();

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 2xl:grid-cols-4 gap-5">
      {data?.map((exam) => {
        return <ExamSummaryCard key={exam.id} exam={exam} />;
      })}
    </div>
  );
};

export default HomePage;
