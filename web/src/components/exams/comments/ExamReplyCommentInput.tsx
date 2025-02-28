import useCreateExamComment from '@/hooks/api/exam/comment/useCreateExamComment';
import useGetExamReplyComments from '@/hooks/api/exam/comment/useGetExamReplyComments';
import useGetExamRootComments from '@/hooks/api/exam/comment/useGetExamRootComments';
import useUser from '@/hooks/useUser';
import { Avatar, Input, Spinner } from '@heroui/react';
import { useQueryClient } from '@tanstack/react-query';
import { useState } from 'react';
import { HiCheck } from 'react-icons/hi';
import { HiOutlineX } from 'react-icons/hi';
import { toast } from 'sonner';

interface ExamReplyCommentInputProps {
  examId: number;
  parentCommentId: number;
  onCancel: () => void;
  onCommentCreated: () => void;
}

const ExamReplyCommentInput = ({
  examId,
  parentCommentId,
  onCancel,
  onCommentCreated,
}: ExamReplyCommentInputProps) => {
  const user = useUser();
  const [content, setContent] = useState('');
  const [isEditing, setIsEditing] = useState(false);
  const queryClient = useQueryClient();

  const { mutate: createComment, isPending } = useCreateExamComment({
    onSuccess: () => {
      toast.success('답글을 성공적으로 작성했습니다.');
      queryClient.invalidateQueries({
        queryKey: useGetExamRootComments.getKey(examId),
        refetchType: 'all',
      });
      queryClient.invalidateQueries({
        queryKey: useGetExamReplyComments.getKey(parentCommentId),
        refetchType: 'all',
      });
      onCommentCreated();
    },
    onError: (e) => {
      toast.error(
        e.message ? e.message : '답글 작성 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.',
      );
    },
    onSettled: () => {
      setContent('');
    },
  });

  const trimmedContent = content.trim();

  const handleFocus = () => {
    if (isEditing) return;

    setIsEditing(true);
  };

  const handleSubmit = () => {
    if (!trimmedContent) return;

    setIsEditing(false);
    createComment({
      examId,
      request: {
        content: trimmedContent,
        parentCommentId,
      },
    });
  };

  const handleBlur = () => {
    setIsEditing(false);
  };

  const handleCancel = () => {
    if (!isEditing) return;

    setIsEditing(false);
    setContent('');
    onCancel();
  };

  if (isPending) {
    return (
      <div className="flex justify-center items-center h-20">
        <Spinner />
      </div>
    );
  }

  return (
    <div className="flex space-x-4 pt-4 pb-2">
      <div className="flex-shrink-0">
        <Avatar src={user.avatarUrl} alt={user.name} size="md" />
      </div>
      <div className="relative w-full" onFocus={handleFocus} onBlur={handleBlur}>
        <Input
          variant="underlined"
          placeholder="댓글 추가..."
          value={content}
          onChange={(e) => setContent(e.target.value)}
        />
        {isEditing && (
          <div className="flex gap-2 mt-2 w-full justify-end items-center absolute right-0">
            <button
              onMouseDown={(e) => {
                e.preventDefault();
              }}
              onClick={handleSubmit}
              disabled={!trimmedContent}
              className={`flex items-center justify-center rounded-lg bg-success text-white w-8 h-8 shadow-md hover:shadow-lg transition-shadow ${
                !content.trim() ? 'opacity-50' : ''
              }`}
            >
              <HiCheck size={20} />
            </button>
            <button
              onMouseDown={(e) => {
                e.preventDefault();
              }}
              onClick={handleCancel}
              className="flex items-center justify-center rounded-lg bg-danger text-white w-8 h-8 shadow-md hover:shadow-lg transition-shadow"
            >
              <HiOutlineX size={20} />
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default ExamReplyCommentInput;
