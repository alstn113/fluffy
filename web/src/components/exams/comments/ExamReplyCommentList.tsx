import useGetExamReplyComments from '@/hooks/api/exam/comment/useGetExamReplyComments';
import ExamReplyCommentItem from './ExamReplyCommentItem';

interface ExamReplyCommentListProps {
  rootCommentId: number;
  examId: number;
}

const ExamReplyCommentList = ({ rootCommentId, examId }: ExamReplyCommentListProps) => {
  const { data: replyComments } = useGetExamReplyComments(rootCommentId);

  return (
    <div>
      {replyComments.replies.map((reply) => (
        <ExamReplyCommentItem key={reply.id} reply={reply} examId={examId} />
      ))}
    </div>
  );
};

export default ExamReplyCommentList;
