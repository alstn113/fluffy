import { Pagination } from '@heroui/react';
import PublishedExamCard from './PublishedExamCard';
import useGetMyExamSummaries from '@/hooks/api/exam/useGetMyExamSummaries';
import { useState } from 'react';

const PublishedExamContent = () => {
  const [page, setPage] = useState(1);
  const { data } = useGetMyExamSummaries('PUBLISHED', page - 1);
  const { content, pageInfo } = data;

  if (!content || pageInfo.totalElements === 0) {
    return (
      <div className="w-full text-center text-xl text-gray-500 mt-24">
        출제 완료된 시험이 없습니다.
      </div>
    );
  }

  return (
    <>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 2xl:grid-cols-4 gap-5">
        {content.map((exam) => {
          return <PublishedExamCard key={exam.id} exam={exam} />;
        })}
      </div>
      <div className="flex justify-center mt-10">
        <Pagination
          color="primary"
          showControls
          showShadow
          page={page}
          total={pageInfo.totalPages}
          onChange={(page) => setPage(page)}
        />
      </div>
    </>
  );
};

export default PublishedExamContent;
