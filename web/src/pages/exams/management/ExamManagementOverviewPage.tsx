import { Divider } from '@nextui-org/react';
import { useParams } from 'react-router';
import useGetExamWithAnswers from '@/hooks/api/exam/useGetExamWithAnswers';
import AsyncBoundary from '@/components/AsyncBoundary';
import formatDate from '@/lib/formatDate';
import ExamPublishButton from '@/components/overview/ExamPublishButton';
import ExamTitleOverview from '@/components/overview/ExamTitleOverview';
import ExamDescriptionOverview from '@/components/overview/ExamDescriptionOverview';

const ExamManagementOverviewPage = () => {
  const params = useParams() as { examId: string };
  const examId = Number(params.examId);

  return (
    <div className="w-full h-full flex items-center flex-col gap-4">
      <div className="max-w-3xl w-full p-8 flex flex-col">
        <AsyncBoundary>
          <ExamManagementOverviewContent examId={examId} />
        </AsyncBoundary>
      </div>
    </div>
  );
};

const ExamManagementOverviewContent = ({ examId }: { examId: number }) => {
  const { data } = useGetExamWithAnswers(examId);
  const isPublished = data.status === 'PUBLISHED';

  return (
    <>
      <div className="flex flex-col gap-2 w-full">
        <ExamTitleOverview isPublished={isPublished} examId={examId} title={data.title} />
      </div>
      <Divider className="my-6" />
      <div className="flex flex-col gap-2 w-full">
        <div className="flex gap-4">
          <div className="min-w-32 font-semibold">시험 문항 수</div>
          <div className="text-gray-500">{data.questions.length} 문항</div>
        </div>
        <div className="flex gap-4">
          <div className="min-w-32 font-semibold">최초 작성일</div>
          <div className="text-gray-500">{formatDate(data.createdAt)}</div>
        </div>
        <div className="flex gap-4">
          <div className="min-w-32 font-semibold">마지막 수정일</div>
          <div className="text-gray-500">{formatDate(data.updatedAt)}</div>
        </div>
        <div className="flex gap-4">
          <div className="min-w-32 font-semibold">시험 상태</div>
          <div className="text-gray-500">
            {data.status === 'PUBLISHED' ? '출제 완료' : '출제 준비중'}
          </div>
        </div>
      </div>
      <Divider className="my-6" />
      <div className="flex flex-col gap-2 w-full">
        <ExamDescriptionOverview
          isPublished={isPublished}
          examId={examId}
          description={data.description}
        />
      </div>
      {!isPublished && (
        <div className="w-full flex justify-center mt-4">
          <ExamPublishButton examId={examId} />
        </div>
      )}
    </>
  );
};

export default ExamManagementOverviewPage;
