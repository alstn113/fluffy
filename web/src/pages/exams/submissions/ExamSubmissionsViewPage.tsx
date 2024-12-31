import AsyncBoundary from '@/components/AsyncBoundary';
import ExamSubmissionsViewContent from '@/components/submissions/ExamSubmissionsViewContent';
import { useState } from 'react';
import { useParams } from 'react-router';

const ExamSubmissionsViewPage = () => {
  const params = useParams() as { examId: string };
  const examId = Number(params.examId);

  const [current, setCurrent] = useState<{
    currentIndex: number;
    currentSubmissionId: number | null;
  }>({
    currentIndex: 0,
    currentSubmissionId: null,
  });

  return (
    <div className="flex flex-row overflow-y-auto h-full w-full">
      <AsyncBoundary>
        <ExamSubmissionsViewContent
          examId={examId}
          current={current}
          onChangeCurrent={setCurrent}
        />
      </AsyncBoundary>
    </div>
  );
};

export default ExamSubmissionsViewPage;
