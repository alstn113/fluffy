import useGetSubmissionDetail from '@/hooks/api/submission/useGetSubmissionDetail';

const SubmissionWithAnswerViewPanel = ({
  examId,
  submissionId,
}: {
  examId: number;
  submissionId: number;
}) => {
  const { data } = useGetSubmissionDetail(examId, submissionId);

  return <div>{JSON.stringify(data)}</div>;
};

export default SubmissionWithAnswerViewPanel;
