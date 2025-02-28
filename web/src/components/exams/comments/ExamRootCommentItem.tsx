import { ExamRootCommentResponse } from '@/api/examCommentAPI';
import { fromNowDate } from '@/lib/date';
import { Avatar } from '@heroui/react';
import { useState } from 'react';
import { SlArrowUp, SlArrowDown, SlPencil, SlTrash } from 'react-icons/sl';

import AsyncBoundary from '@/components/AsyncBoundary';
import ExamReplyCommentList from './ExamReplyCommentList';
import ExamReplyCommentInput from './ExamReplyCommentInput';
import useUser from '@/hooks/useUser';
import MoreVertMenu from './MoreVertMenu';

interface ExamRootCommentItemProps {
  examId: number;
  comment: ExamRootCommentResponse;
}

const ExamRootCommentItem = ({ examId, comment }: ExamRootCommentItemProps) => {
  const user = useUser();
  const isMyComment = user?.id === comment.author.id;

  const [replyVisible, setReplyVisible] = useState(false);
  const [replyInputVisible, setReplyInputVisible] = useState(false);

  const moreVertMenuItems = [
    {
      icon: <SlTrash size={16} />,
      name: '삭제',
      onClick: () => {
        console.log('삭제');
      },
    },
  ];

  return (
    <div className="flex items-start space-x-4 py-3">
      {/* 왼쪽 - 아바타 이미지 */}
      <div className="flex-shrink-0">
        <Avatar src={comment.author.avatarUrl} alt={comment.author.name} size="md" />
      </div>

      {/* 중앙 - 댓글 내용 */}
      <div className="flex-1 min-w-0">
        {comment.isDeleted ? (
          <div className="text-gray-500 font-semibold">삭제된 댓글입니다.</div>
        ) : (
          <>
            <div className="flex items-center space-x-2">
              <span className="font-semibold text-sm">{comment.author.name}</span>
              <span className="text-xs text-gray-500">{fromNowDate(comment.createdAt)}</span>
            </div>
            <p className="mt-1 text-sm text-gray-800 break-words">{comment.content}</p>
          </>
        )}

        {/* 하단 툴바 */}
        {!comment.isDeleted && (
          <>
            <div className="flex items-center space-x-2 mt-2">
              <button
                className="text-sm text-gray-500"
                onClick={() => setReplyInputVisible(!replyInputVisible)}
              >
                답글
              </button>
            </div>
            <div>
              {replyInputVisible && (
                <ExamReplyCommentInput
                  examId={examId}
                  parentCommentId={comment.id}
                  onCancel={() => setReplyInputVisible(false)}
                  onCommentCreated={() => {
                    setReplyInputVisible(false);
                    setReplyVisible(true);
                  }}
                />
              )}
            </div>
          </>
        )}
        {comment.replyCount > 0 && (
          <>
            <button
              className="flex items-center text-sm font-medium text-blue-600 mt-2"
              onClick={() => setReplyVisible(!replyVisible)}
            >
              {replyVisible ? (
                <SlArrowUp size={14} className="mr-2" />
              ) : (
                <SlArrowDown size={14} className="mr-2" />
              )}
              답글 {comment.replyCount}개
            </button>
            {/* 답글 목록 */}
            {replyVisible && (
              <div className="mt-2 w-full">
                <AsyncBoundary
                  ErrorFallbackComponent={() => (
                    <div className="text-red-500 text-sm">
                      답글을 불러오는 중에 오류가 발생했습니다.
                    </div>
                  )}
                >
                  <ExamReplyCommentList rootCommentId={comment.id} examId={examId} />
                </AsyncBoundary>
              </div>
            )}
          </>
        )}
      </div>

      {/* 오른쪽 - 더보기 버튼 (본인 댓글일 경우만 표시) */}
      {isMyComment && (
        <div className="flex-shrink-0">
          <MoreVertMenu items={moreVertMenuItems} />
        </div>
      )}
    </div>
  );
};

export default ExamRootCommentItem;
