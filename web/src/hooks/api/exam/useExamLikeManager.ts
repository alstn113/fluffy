import { useMutation, useQueryClient } from '@tanstack/react-query';
import useUser from '@/hooks/useUser.ts';
import { useRef, useState } from 'react';
import toast from 'react-hot-toast';
import useGetExamDetailSummary from '@/hooks/api/exam/useGetExamDetailSummary.ts';
import { ExamAPI, ExamDetailSummaryResponse } from '@/api/examAPI';

interface UseExamLikeManagerProps {
  examId: number;
  initialIsLiked: boolean;
  initialLikeCount: number;
}

const useExamLikeManager = ({
  examId,
  initialIsLiked,
  initialLikeCount,
}: UseExamLikeManagerProps) => {
  const queryClient = useQueryClient();
  const user = useUser();

  const [isLiked, setIsLiked] = useState<boolean>(initialIsLiked);
  const [likeCount, setLikeCount] = useState<number>(initialLikeCount);

  const debounceTimeout = useRef<number | null>(null);

  const debounceInvalidateQueries = () => {
    if (debounceTimeout.current) {
      clearTimeout(debounceTimeout.current);
    }
    debounceTimeout.current = setTimeout(() => {
      queryClient.invalidateQueries({
        queryKey: useGetExamDetailSummary.getKey(examId),
        refetchType: 'all',
      });
    }, 300);
  };

  const useLikeMutation = (
    mutationFunction: (examId: number) => Promise<void>,
    isLikeAction: boolean,
  ) => {
    return useMutation({
      mutationFn: mutationFunction,
      onMutate: async () => {
        await queryClient.cancelQueries({
          queryKey: useGetExamDetailSummary.getKey(examId),
        });

        setIsLiked(isLikeAction);
        setLikeCount((prevCount) => prevCount + (isLikeAction ? 1 : -1));

        const previousData = queryClient.getQueryData<ExamDetailSummaryResponse>(
          useGetExamDetailSummary.getKey(examId),
        );

        return { previousData };
      },
      onError: (_error, _variables, context) => {
        if (context) {
          queryClient.setQueryData(useGetExamDetailSummary.getKey(examId), context.previousData);
        }

        toast.error(`좋아요${isLikeAction ? '에' : ' 취소에'} 실패했습니다.`);

        setIsLiked(!isLikeAction);
        setLikeCount((prevCount) => prevCount - (isLikeAction ? 1 : -1));
      },
      onSettled: debounceInvalidateQueries,
    });
  };

  const { mutate: like } = useLikeMutation(ExamAPI.like, true);
  const { mutate: unlike } = useLikeMutation(ExamAPI.unlike, false);

  const toggleLike = () => {
    if (!user) {
      toast.error('좋아요를 누르려면 로그인이 필요합니다.');
      return;
    }

    if (isLiked) {
      unlike(examId);
      return;
    }

    like(examId);
  };

  return {
    isLiked,
    likeCount,
    toggleLike,
  };
};

export default useExamLikeManager;
