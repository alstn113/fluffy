import { useSearchParams } from 'react-router';
import AsyncBoundary from '../AsyncBoundary';
import SubmissionDetailsContent from './SubmissionDetailContent';

interface SubmissionDetailsProps {
  examId: number;
}

const SubmissionDetails = ({ examId }: SubmissionDetailsProps) => {
  const [searchParams] = useSearchParams();
  const submissionId = Number(searchParams.get('submissionId'));

  return (
    <div className="w-full h-full flex items-center flex-col gap-4">
      <AsyncBoundary>
        <SubmissionDetailsContent examId={examId} submissionId={submissionId} />
      </AsyncBoundary>
    </div>
  );
};

export default SubmissionDetails;
