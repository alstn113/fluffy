import AsyncBoundary from '@/components/AsyncBoundary';
import useGetExamSummaries from '@/hooks/api/exam/useGetExamSummaries';
import ExamSummaryCard from '@/components/exams/ExamSummaryCard';
import { useState } from 'react';
import { Pagination } from '@nextui-org/react';

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
  const [page, setPage] = useState(1);
  const { data } = useGetExamSummaries(page - 1); // page는 0부터 시작
  const { content, pageInfo } = data;

  return (
    <>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 2xl:grid-cols-4 gap-5">
        {content.map((exam) => {
          return <ExamSummaryCard key={exam.id} exam={exam} />;
        })}
      </div>
      <div className="flex justify-center mt-5">
        <Pagination
          color="secondary"
          showControls
          page={page}
          total={pageInfo.totalPages}
          onChange={(page) => setPage(page)}
        />
      </div>
    </>
  );
};

export default HomePage;
