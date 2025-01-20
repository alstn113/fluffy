import { useQueryClient } from '@tanstack/react-query';
import useUser from '@/hooks/useUser.ts';
import { useEffect, useState } from 'react';
import useLikeExam from '@/hooks/api/exam/like/useLikeExam.ts';
import useUnlikeExam from '@/hooks/api/exam/like/useUnlikeExam.ts';
import toast from 'react-hot-toast';
import useGetExamDetailSummary from '@/hooks/api/exam/useGetExamDetailSummary.ts';

interface useExamLikeManagerProps {
  examId: number;
  initialIsLiked: boolean;
  initialLikeCount: number;
}

const useExamLikeManager = ({
  examId,
  initialIsLiked,
  initialLikeCount,
}: useExamLikeManagerProps) => {
  const queryClient = useQueryClient();
  const user = useUser();

  const [isLiked, setIsLiked] = useState<boolean>(initialIsLiked);
  const [likeCount, setLikeCount] = useState<number>(initialLikeCount);

  const { mutate: likeExam } = useLikeExam();
  const { mutate: unlikeExam } = useUnlikeExam();

  useEffect(() => {
    setLikeCount(initialLikeCount);
  }, [initialLikeCount]);

  const toggleLike = () => {
    if (!user) {
      toast.error('로그인이 필요합니다.');
    }

    if (isLiked) {
      handleLike();
      return;
    }

    handleUnlike();
  };

  const handleLike = () => {
    setLikeCount(likeCount + 1);
    setIsLiked(true);

    likeExam(examId, {
      onSuccess: async () => {
        await queryClient.invalidateQueries({
          queryKey: useGetExamDetailSummary.getKey(examId),
          exact: true,
          refetchType: 'all',
        });
      },
    });
  };

  const handleUnlike = () => {
    setLikeCount(likeCount - 1);
    setIsLiked(false);

    unlikeExam(examId, {
      onSuccess: async () => {
        await queryClient.invalidateQueries({
          queryKey: useGetExamDetailSummary.getKey(examId),
          exact: true,
          refetchType: 'all',
        });
      },
    });
  };

  return {
    isLiked,
    likeCount,
    toggleLike,
  };
};

export default useExamLikeManager;
