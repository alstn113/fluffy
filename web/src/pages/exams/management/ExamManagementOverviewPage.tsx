import { Divider } from '@nextui-org/react';
import { useParams } from 'react-router';
import useGetExamWithAnswers from '@/hooks/api/exam/useGetExamWithAnswers';
import AsyncBoundary from '@/components/AsyncBoundary';
import formatDate from '@/lib/formatDate';
import useUpdateExamTitle from '@/hooks/api/exam/useUpdateExamTitle';
import useUpdateExamDescription from '@/hooks/api/exam/useUpdateExamDescription';
import EditableInput from '@/components/overview/EditableInput.tsx';
import { useQueryClient } from '@tanstack/react-query';
import ExamPublishButton from '@/components/overview/ExamPublishButton';

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

  const { mutate: updateTitleMutate } = useUpdateExamTitle();
  const { mutate: updateDescriptionMutate } = useUpdateExamDescription();

  const queryclient = useQueryClient();

  const handleUpdateTitle = (title: string) => {
    updateTitleMutate(
      { examId, request: { title } },
      {
        onSuccess: () => {
          queryclient.invalidateQueries({
            queryKey: useGetExamWithAnswers.getKey(examId),
          });
        },
      },
    );
  };

  const handleUpdateDescription = (description: string) => {
    updateDescriptionMutate({ examId, request: { description } });
  };

  return (
    <>
      <div className="flex flex-col gap-2 w-full">
        <div className="font-semibold text-2xl">시험 제목</div>
        <EditableInput initialValue={data.title} onSave={handleUpdateTitle} maxLength={100} />
      </div>
      <Divider className="my-8" />
      <div className="flex gap-4">
        <div className="p-1 min-w-32 font-semibold">시험 문항 수</div>
        <div className="p-1 text-gray-500">{data.questions.length} 문항</div>
      </div>
      <div className="flex gap-4">
        <div className="p-1 min-w-32 font-semibold">최초 작성일</div>
        <div className="p-1 text-gray-500">{formatDate(data.createdAt)}</div>
      </div>
      <div className="flex gap-4">
        <div className="p-1 min-w-32 font-semibold">마지막 수정일</div>
        <div className="p-1 text-gray-500">{formatDate(data.updatedAt)}</div>
      </div>
      <div className="flex gap-4">
        <div className="p-1 min-w-32 font-semibold">시험 상태</div>
        <div className="p-1 text-gray-500">{data.status === 'PUBLISHED' ? '출제 완료' : '출제 준비중'}</div>
      </div>
      <Divider className="my-8" />
      <div className="flex flex-col gap-2 w-full">
        <div className="font-semibold">시험 설명</div>
        <EditableInput
          initialValue={data.description}
          onSave={handleUpdateDescription}
          placeholder="시험 설명을 입력할 수 있습니다."
          isTextarea
          maxLength={2000}
        />
      </div>
      <div className="w-full flex justify-center mt-4">
        <ExamPublishButton examId={examId} />
      </div>
    </>
  );
};

export default ExamManagementOverviewPage;
