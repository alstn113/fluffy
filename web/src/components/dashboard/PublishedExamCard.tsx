import { ExamSummaryResponse } from '@/api/examAPI';
import { Routes } from '@/constants';
import formatDate from '@/lib/formatDate';
import {
  Card,
  CardBody,
  CardFooter,
  CardHeader,
  Chip,
  Divider,
  Image,
  Link,
} from '@nextui-org/react';

interface ExamSummaryCardProps {
  exam: ExamSummaryResponse;
}

const PublishedExamCard = ({ exam }: ExamSummaryCardProps) => {
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
          <Chip color="success" variant="shadow" classNames={{ content: 'text-white' }}>
            출제 완료
          </Chip>
        </div>
      </CardHeader>
      <Divider />
      <CardBody>
        <h3 className="text-lg font-semibold">{exam.title}</h3>
        <p className="text-default-500 line-clamp-3">{exam.description}</p>
      </CardBody>
      <Divider />
      <CardFooter className="flex justify-between">
        <div>문제 수: {exam.questionCount}</div>
        <Link href={Routes.exam.management.overview(exam.id)}>정보 확인하기</Link>
      </CardFooter>
    </Card>
  );
};

export default PublishedExamCard;
