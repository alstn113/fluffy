import useGetMyExamSummaries from '@/hooks/api/exam/useGetMyExamSummaries';
import { useState } from 'react';
import DraftExamCard from './DraftExamCard';
import { Pagination } from '@nextui-org/react';

const DraftExamContent = () => {
  const [page, setPage] = useState(1);
  const { data } = useGetMyExamSummaries('DRAFT', page - 1);
  const { content, pageInfo } = data;

  if (!content || pageInfo.totalElements === 0) {
    return (
      <div className="w-full text-center text-xl text-gray-500 mt-24">
        출제 준비 중인 시험이 없습니다.
      </div>
    );
  }

  return (
    <>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 2xl:grid-cols-4 gap-5">
        {content.map((exam) => {
          return <DraftExamCard key={exam.id} exam={exam} />;
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

export default DraftExamContent;
