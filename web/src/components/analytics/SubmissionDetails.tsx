import useGetSubmissionDetail from '@/hooks/api/submission/useGetSubmissionDetail';
import { fromNowDate } from '@/lib/date.ts';
import { Divider, User } from '@nextui-org/react';
import { useSearchParams } from 'react-router';
import SubmissionAnswers from './SubmissionAnswers';
import AsyncBoundary from '../AsyncBoundary';

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

const SubmissionDetailsContent = ({
  examId,
  submissionId,
}: {
  examId: number;
  submissionId: number;
}) => {
  const { data } = useGetSubmissionDetail(examId, submissionId);

  return (
    <>
      <div className="w-full flex gap-8 justify-between items-center p-4 rounded-lg shadow-md shadow-gray-200">
        <User
          avatarProps={{ radius: 'lg', src: data.participant.avatarUrl }}
          description={data.participant.email}
          name={data.participant.name}
        />
        <div className="flex flex-col">
          <div className="text-ㅡ">제출일</div>
          <div className="text-gray-500 text-sm">{fromNowDate(data.submittedAt)}</div>
        </div>
      </div>
      {data.answers.map((answer, index) => (
        <SubmissionAnswers key={answer.id} answer={answer} index={index} />
      ))}
      <Divider className="my-4" />
    </>
  );
};

export default SubmissionDetails;
