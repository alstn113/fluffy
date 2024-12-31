import useGetSubmissionDetail from '@/hooks/api/submission/useGetSubmissionDetail';
import { fromNowDate } from '@/lib/date.ts';
import { User } from '@nextui-org/react';
import SubmissionAnswers from './SubmissionAnswers';

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
      <div className="w-full h-full overflow-y-auto flex flex-col gap-4">
        {data.answers.map((answer, index) => (
          <SubmissionAnswers key={answer.id} answer={answer} index={index} />
        ))}
      </div>
      <div className="mt-4" />
    </>
  );
};

export default SubmissionDetailsContent;
