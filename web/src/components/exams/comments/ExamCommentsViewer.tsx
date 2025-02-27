import useGetExamRootComments from '@/hooks/api/exam/comment/useGetExamRootComments';
import ExamRootCommentItem from './ExamRootCommentItem';
import ExamCommentInput from './ExamCommentInput';

interface ExamCommentsViewerProps {
  examId: number;
}

const ExamCommentsViewer = ({ examId }: ExamCommentsViewerProps) => {
  const { data: rootComments } = useGetExamRootComments(examId);

  return (
    <div className="mt-8">
      <div className="flex items-center mb-4">
        <div className="text-xl font-bold">댓글 {rootComments.comments.length}개</div>
      </div>
      <ExamCommentInput examId={examId} />
      {rootComments.comments.map((comment) => (
        <ExamRootCommentItem key={comment.id} examId={examId} comment={comment} />
      ))}
    </div>
  );
};

export default ExamCommentsViewer;
