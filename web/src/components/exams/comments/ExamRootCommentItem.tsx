import { ExamRootCommentResponse } from '@/api/examCommentAPI';
import { fromNowDate } from '@/lib/date';
import { Avatar } from '@heroui/react';
import { useState } from 'react';
import { SlArrowUp, SlArrowDown } from 'react-icons/sl';
import ExamReplyCommentList from './ExamReplyCommentList';
import AsyncBoundary from '@/components/AsyncBoundary';

interface ExamRootCommentItemProps {
  examId: number;
  comment: ExamRootCommentResponse;
}

const ExamRootCommentItem = ({ examId, comment }: ExamRootCommentItemProps) => {
  const [replyVisible, setReplyVisible] = useState(false);

  return (
    <>
      <div className="flex space-x-4 py-2">
        {/* 프로필 이미지 */}
        <div className="flex-shrink-0">
          <Avatar src={comment.author.avatarUrl} alt={comment.author.name} size="md" />
        </div>

        {/* 댓글 내용 */}
        <div className="flex-1">
          {comment.isDeleted ? (
            <>
              <div className="text-gray-500">삭제된 댓글입니다.</div>
            </>
          ) : (
            <>
              <div className="flex items-center space-x-2">
                <span className="font-semibold text-sm">{comment.author.name}</span>
                <span className="text-xs text-gray-500">{fromNowDate(comment.createdAt)}</span>
              </div>
              <p className="mt-1 text-sm text-gray-800">{comment.content}</p>
            </>
          )}

          {/* 답글 보기/숨기기 버튼 */}
          {comment.replyCount > 0 && (
            <button
              className="flex mt-2 text-blue-600 text-sm font-medium items-center"
              onClick={() => setReplyVisible(!replyVisible)}
            >
              {replyVisible ? (
                <SlArrowUp size={14} className="mr-2" />
              ) : (
                <SlArrowDown size={14} className="mr-2" />
              )}
              답글 {comment.replyCount}개
            </button>
          )}
        </div>
      </div>
      {replyVisible && (
        <div className="ml-14">
          {/* 답글 목록 */}
          <AsyncBoundary
            ErrorFallbackComponent={() => <div>답글을 불러오는 중에 오류가 발생했습니다.</div>}
          >
            <ExamReplyCommentList rootCommentId={comment.id} examId={examId} />
          </AsyncBoundary>
        </div>
      )}
    </>
  );
};

export default ExamRootCommentItem;
