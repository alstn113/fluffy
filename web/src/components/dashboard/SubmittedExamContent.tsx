import { Pagination } from '@nextui-org/react';
import { useState } from 'react';
import useGetSubmittedExamSummaries from '@/hooks/api/exam/useGetSubmittedExamSummaries';
import SubmittedExamCard from './SubmittedExamCard';

const SubmittedExamContent = () => {
  const [page, setPage] = useState(1);
  const { data } = useGetSubmittedExamSummaries(page - 1);
  const { content, pageInfo } = data;

  if (!content || pageInfo.totalElements === 0) {
    return (
      <div className="w-full text-center text-xl text-gray-500 mt-24">제출한 시험이 없습니다.</div>
    );
  }

  return (
    <>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 2xl:grid-cols-4 gap-5">
        {content.map((submittedExam) => {
          return <SubmittedExamCard key={submittedExam.examId} submittedExam={submittedExam} />;
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

export default SubmittedExamContent;
