import { useMutation, useQueryClient } from '@tanstack/react-query';
import useUser from '@/hooks/useUser.ts';
import { useRef, useState } from 'react';
import toast from 'react-hot-toast';
import useGetExamDetailSummary from '@/hooks/api/exam/useGetExamDetailSummary.ts';
import { ExamAPI, ExamDetailSummaryResponse } from '@/api/examAPI';

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

  const debounceTimeout = useRef<number | null>(null);

  const invalidateQueryDebounced = () => {
    if (debounceTimeout.current) {
      clearTimeout(debounceTimeout.current);
    }
    debounceTimeout.current = setTimeout(async () => {
      await queryClient.invalidateQueries({
        queryKey: useGetExamDetailSummary.getKey(examId),
        refetchType: 'all',
      });
    }, 300);
  };

  const { mutate: likeExam } = useMutation({
    mutationFn: ExamAPI.like,
    onMutate: async () => {
      await queryClient.cancelQueries({
        queryKey: useGetExamDetailSummary.getKey(examId),
      });

      const prevData = queryClient.getQueryData<ExamDetailSummaryResponse>(
        useGetExamDetailSummary.getKey(examId),
      );

      setIsLiked(true);
      setLikeCount(likeCount + 1);

      return prevData;
    },
    onError: (_error, _variables, context) => {
      if (context) {
        queryClient.setQueryData(useGetExamDetailSummary.getKey(examId), context);
      }

      setIsLiked(false);
      setLikeCount(likeCount - 1);
    },
    onSettled: async () => {
      invalidateQueryDebounced();
    },
  });

  const { mutate: unlikeExam } = useMutation({
    mutationFn: ExamAPI.unlike,
    onMutate: async () => {
      await queryClient.cancelQueries({
        queryKey: useGetExamDetailSummary.getKey(examId),
      });

      const prevData = queryClient.getQueryData<ExamDetailSummaryResponse>(
        useGetExamDetailSummary.getKey(examId),
      );

      setIsLiked(false);
      setLikeCount(likeCount - 1);

      return prevData;
    },
    onError: (_error, _variables, context) => {
      if (context) {
        queryClient.setQueryData(useGetExamDetailSummary.getKey(examId), context);
      }

      setIsLiked(true);
      setLikeCount(likeCount + 1);
    },
    onSettled: async () => {
      invalidateQueryDebounced();
    },
  });

  const toggleLike = () => {
    if (!user) {
      toast.error('로그인이 필요합니다.');
    }

    if (isLiked) {
      unlikeExam(examId);
      return;
    }

    likeExam(examId);
  };

  return {
    isLiked,
    likeCount,
    toggleLike,
  };
};

export default useExamLikeManager;
