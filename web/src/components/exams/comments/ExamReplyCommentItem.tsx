import { ExamReplyCommentResponse } from '@/api/examCommentAPI';
import { fromNowDate } from '@/lib/date';
import { Avatar } from '@heroui/react';

interface ExamReplyCommentItemProps {
  examId: number;
  reply: ExamReplyCommentResponse;
}

const ExamReplyCommentItem = ({ examId, reply }: ExamReplyCommentItemProps) => {
  return (
    <div className="flex space-x-3 py-2">
      {/* 프로필 이미지 */}
      <div className="flex-shrink-0">
        <Avatar src={reply.author.avatarUrl} alt={reply.author.name} size="sm" />
      </div>

      {/* 댓글 내용 */}
      <div className="flex-1">
        <div className="flex items-center space-x-2">
          <span className="font-semibold text-sm">{reply.author.name}</span>
          <span className="text-xs text-gray-500">{fromNowDate(reply.createdAt)}</span>
        </div>
        <p className="mt-1 text-sm text-gray-800">{reply.content}</p>
      </div>
    </div>
  );
};

export default ExamReplyCommentItem;
