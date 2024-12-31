import { SubmittedExamSummaryResponse } from '@/api/examAPI';
import { Routes } from '@/constants';
import { fromNowDate } from '@/lib/date.ts';
import { Card, CardBody, CardFooter, CardHeader, Divider, Image, Link } from '@nextui-org/react';

interface SubmittedExamCardProps {
  submittedExam: SubmittedExamSummaryResponse;
}

const SubmittedExamCard = ({ submittedExam }: SubmittedExamCardProps) => {
  return (
    <Card className="max-w-[400px]">
      <CardHeader className="flex gap-3">
        <Image
          alt="avatar url"
          height={40}
          radius="sm"
          src={submittedExam.author.avatarUrl}
          width={40}
        />
        <div className="flex flex-col">
          <p className="text-md">{submittedExam.author.name}</p>
          <p className="text-small text-default-500">
            마지막 제출일: {fromNowDate(submittedExam.lastSubmissionDate)}
          </p>
        </div>
      </CardHeader>
      <Divider />
      <CardBody>
        <h3 className="text-lg font-semibold">{submittedExam.title}</h3>
        <p className="text-default-500 line-clamp-3">{submittedExam.description}</p>
      </CardBody>
      <Divider />
      <CardFooter className="flex justify-between">
        <div>{submittedExam.submissionCount}회 제출됨</div>
        <Link href={Routes.exam.submissions(submittedExam.examId)}>정보 확인하기</Link>
      </CardFooter>
    </Card>
  );
};

export default SubmittedExamCard;
