import { ExamSummaryResponse } from '@/api/examAPI';
import formatDate from '@/lib/formatDate';
import {
  Card,
  CardHeader,
  CardBody,
  CardFooter,
  Image,
  Divider,
  Link,
  Chip,
} from '@nextui-org/react';

interface ExamSummaryCardProps {
  exam: ExamSummaryResponse;
}

const DraftExamCard = ({ exam }: ExamSummaryCardProps) => {
  return (
    <Card className="max-w-[400px]">
      <CardHeader className="flex gap-3">
        <Image alt="avatar url" height={40} radius="sm" src={exam.author.avatarUrl} width={40} />
        <div className="flex flex-col">
          <p className="text-md">{exam.author.name}</p>
          <p className="text-small text-default-500">생성 시간: {formatDate(exam.createdAt)}</p>
          <p className="text-small text-default-500">수정 시간: {formatDate(exam.updatedAt)}</p>
        </div>
        <div className="ml-auto mr-4 flex items-center">
          <Chip color="warning" variant="shadow" classNames={{ content: 'text-white' }}>
            출제 준비
          </Chip>
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
        <Link href={`/exams/${exam.id}/edit`}>이어서 편집하기</Link>
      </CardFooter>
    </Card>
  );
};

export default DraftExamCard;