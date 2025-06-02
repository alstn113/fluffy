import AsyncBoundary from '@/components/AsyncBoundary';
import useGetExamSummaries from '@/hooks/api/exam/useGetExamSummaries';
import ExamSummaryCard from '@/components/exams/ExamSummaryCard';
import { Pagination } from '@heroui/react';
import { useSearchParams } from 'react-router';

const HomePage = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const pageFromUrl = parseInt(searchParams.get('page') ?? '1', 10);
  const handleChangePage = (newPage: number) => {
    setSearchParams({ page: newPage.toString() });
  };

  return (
    <div className="container mx-auto px-5 py-12">
      <div className="text-2xl font-semibold mb-5">최근 시험 목록</div>
      <AsyncBoundary>
        <ExamListContent page={pageFromUrl} onChangePage={handleChangePage} />
      </AsyncBoundary>
    </div>
  );
};

interface ExamListContentProps {
  page: number;
  onChangePage: (newPage: number) => void;
}

const ExamListContent = ({ page, onChangePage }: ExamListContentProps) => {
  const { data } = useGetExamSummaries(page - 1);
  const { content, pageInfo } = data;

  return (
    <>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 2xl:grid-cols-4 gap-5">
        {content.map((exam) => {
          return <ExamSummaryCard key={exam.id} exam={exam} />;
        })}
      </div>
      <div className="flex justify-center mt-10">
        <Pagination
          color="primary"
          showControls
          showShadow
          page={page}
          total={pageInfo.totalPages}
          onChange={onChangePage}
        />
      </div>
    </>
  );
};

export default HomePage;
