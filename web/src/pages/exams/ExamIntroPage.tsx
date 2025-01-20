import AsyncBoundary from '@/components/AsyncBoundary';
import { Routes } from '@/constants';
import useUser from '@/hooks/useUser';
import { fullDate } from '@/lib/date.ts';
import { Button, Divider } from '@nextui-org/react';
import toast from 'react-hot-toast';
import { useNavigate, useParams } from 'react-router';
import useGetExamDetailSummary from '@/hooks/api/exam/useGetExamDetailSummary.ts';
import useExamLikeManager from '@/hooks/api/exam/useExamLikeManager';

const ExamIntroPage = () => {
  const params = useParams() as { examId: string };
  const examId = Number(params.examId);

  return (
    <div className="w-full h-full flex items-center flex-col gap-4">
      <div className="max-w-2xl w-full p-8 flex flex-col">
        <AsyncBoundary>
          <ExamProgressContent examId={examId} />
        </AsyncBoundary>
      </div>
    </div>
  );
};

const ExamProgressContent = ({ examId }: { examId: number }) => {
  const user = useUser();
  const { data } = useGetExamDetailSummary(examId);
  const navigate = useNavigate();
  const { isLiked, likeCount, toggleLike } = useExamLikeManager({
    examId,
    initialIsLiked: data.isLiked,
    initialLikeCount: data.likeCount,
  });

  const handleExamStart = () => {
    if (!user) {
      toast.error('로그인이 필요합니다.');
      return;
    }
    navigate(Routes.exam.progress(examId));
  };

  return (
    <>
      <div className="flex flex-col gap-2 w-full">
        <div className="font-semibold text-2xl">시험 제목</div>
        <div className="text-gray-500">{data.title}</div>
      </div>
      <Divider className="my-6" />
      <div className="flex flex-col gap-2 w-full">
        <div className="flex gap-4">
          <div className="min-w-32 font-semibold">시험 문항 수</div>
          <div className="text-gray-500">{data.questionCount} 문항</div>
        </div>
        <div className="flex gap-4">
          <div className="min-w-32 font-semibold">최초 작성일</div>
          <div className="text-gray-500">{fullDate(data.createdAt)}</div>
        </div>
        <div className="flex gap-4">
          <div className="min-w-32 font-semibold">마지막 수정일</div>
          <div className="text-gray-500">{fullDate(data.updatedAt)}</div>
        </div>
        <div className="flex gap-4">
          <div className="min-w-32 font-semibold">시험 상태</div>
          <div className="text-gray-500">출제 완료</div>
        </div>
        <div className="flex gap-4">
          <div>
            <div className="min-w-32 font-semibold">좋아요 수</div>
            <div className="text-gray-500">{likeCount} 개</div>
          </div>
          <div>
            <Button onClick={toggleLike} variant="shadow" color={isLiked ? 'primary' : 'default'}>
              {isLiked ? '좋아요 취소' : '좋아요'}
            </Button>
          </div>
        </div>
      </div>
      <Divider className="my-6" />
      <div className="flex flex-col gap-2 w-full">
        <div className="font-semibold">시험 설명</div>
        <div className="text-gray-500 break-words">{data.description}</div>
      </div>
      <div className="w-full flex justify-center mt-6">
        <Button onClick={handleExamStart} variant="shadow" color="primary">
          시험 응시
        </Button>
      </div>
    </>
  );
};

export default ExamIntroPage;
