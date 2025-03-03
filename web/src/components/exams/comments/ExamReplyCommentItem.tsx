import { ExamReplyCommentResponse } from '@/api/examCommentAPI';
import useDeleteExamComment from '@/hooks/api/exam/comment/useDeleteExamComment';
import useGetExamReplyComments from '@/hooks/api/exam/comment/useGetExamReplyComments';
import useUser from '@/hooks/useUser';
import { fromNowDate } from '@/lib/date';
import { Avatar } from '@heroui/react';
import { useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import MoreVertMenu from './MoreVertMenu';
import { SlTrash } from 'react-icons/sl';
import useGetExamRootComments from '@/hooks/api/exam/comment/useGetExamRootComments';

interface ExamReplyCommentItemProps {
  exmaId: number;
  rootCommentId: number;
  reply: ExamReplyCommentResponse;
}

const ExamReplyCommentItem = ({ exmaId, rootCommentId, reply }: ExamReplyCommentItemProps) => {
  const queryClient = useQueryClient();
  const { user } = useUser();
  const isMyReply = user?.id === reply.author.id;

  const { mutate: deleteReply } = useDeleteExamComment({
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: useGetExamReplyComments.getKey(rootCommentId),
        refetchType: 'all',
      });
      queryClient.invalidateQueries({
        queryKey: useGetExamRootComments.getKey(exmaId),
        refetchType: 'all',
      });
    },
    onError: (e) => {
      toast.error(
        e.message ? e.message : '답글 삭제 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.',
      );
    },
  });

  const moreVertMenuItems = [
    {
      icon: <SlTrash size={16} />,
      name: '삭제',
      onClick: () => {
        deleteReply(reply.id);
      },
    },
  ];

  return (
    <div className="flex items-start space-x-3 py-2">
      {/* 프로필 이미지 */}
      <div className="flex-shrink-0">
        <Avatar src={reply.author.avatarUrl} alt={reply.author.name} size="sm" />
      </div>

      {/* 댓글 내용 */}
      <div className="flex-1 min-w-0">
        <div className="flex items-center space-x-2">
          <span className="font-semibold text-sm">{reply.author.name}</span>
          <span className="text-xs text-gray-500">{fromNowDate(reply.createdAt)}</span>
        </div>
        <p className="mt-1 text-sm text-gray-800 break-words">{reply.content}</p>
      </div>

      {/* 오른쪽 - 더보기 버튼 (본인 댓글만 보임) */}
      {isMyReply && (
        <div className="flex-shrink-0">
          <MoreVertMenu items={moreVertMenuItems} />
        </div>
      )}
    </div>
  );
};

export default ExamReplyCommentItem;
