import { ExamSummaryResponse } from '@/api/examAPI';
import { Routes } from '@/constants';
import formatDate from '@/lib/formatDate';
import { Card, CardBody, CardFooter, CardHeader, Chip, Divider, Image, Link } from '@nextui-org/react';

interface ExamSummaryCardProps {
  exam: ExamSummaryResponse;
}

const ExamSummaryCard = ({ exam }: ExamSummaryCardProps) => {
  return (
    <Card className="max-w-[400px]">
      <CardHeader className="flex gap-3">
        <Image alt="avatar url" height={40} radius="sm" src={exam.author.avatarUrl} width={40} />
        <div className="flex flex-col">
          <p className="text-md">{exam.author.name}</p>
          <p className="text-small text-default-500">{formatDate(exam.createdAt)}</p>
        </div>
        <div className="ml-auto mr-4 flex items-center">
          {exam.status == 'DRAFT' ? (
            <Chip color="warning" variant="shadow" classNames={{ content: 'text-white' }}>
              출제 준비
            </Chip>
          ) : (
            <Chip color="success" variant="shadow" classNames={{ content: 'text-white' }}>
              출제 완료
            </Chip>
          )}
        </div>
      </CardHeader>
      <Divider />
      <CardBody>
        <h3 className="text-lg font-semibold">{exam.title}</h3>
        <h2 className="text-default-500">{exam.description}</h2>
      </CardBody>
      <Divider />
      <CardFooter className="flex justify-between">
        <div>문제 수: {exam.questionCount}</div>
        <Link href={Routes.exam.detail(exam.id)}>시험 보기</Link>
      </CardFooter>
    </Card>
  );
};

export default ExamSummaryCard;
