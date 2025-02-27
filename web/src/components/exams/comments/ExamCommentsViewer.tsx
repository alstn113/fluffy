import useGetExamRootComments from '@/hooks/api/exam/comment/useGetExamRootComments';
import ExamRootCommentItem from './ExamRootCommentItem';

interface ExamCommentsViewerProps {
  examId: number;
}

const ExamCommentsViewer = ({ examId }: ExamCommentsViewerProps) => {
  const { data: rootComments } = useGetExamRootComments(examId);

  return (
    <div>
      <div>Input</div>
      {rootComments.comments.map((comment) => (
        <ExamRootCommentItem key={comment.id} examId={examId} comment={comment} />
      ))}
    </div>
  );
};

export default ExamCommentsViewer;
