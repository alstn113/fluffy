import { ExamSummaryResponse } from '@/api/examAPI';
import { Routes } from '@/constants';
import useUser from '@/hooks/useUser';
import { fromNowDate } from '@/lib/date.ts';
import {
  Button,
  Card,
  CardBody,
  CardFooter,
  CardHeader,
  Chip,
  Divider,
  Image,
} from '@nextui-org/react';
import toast from 'react-hot-toast';
import { useNavigate } from 'react-router';
import CardDescription from '../common/CardDescription';

interface ExamSummaryCardProps {
  exam: ExamSummaryResponse;
}

const ExamSummaryCard = ({ exam }: ExamSummaryCardProps) => {
  const user = useUser();
  const navigate = useNavigate();
  const handleExamStart = () => {
    if (!user) {
      toast.error('로그인이 필요합니다.');
      return;
    }

    navigate(Routes.exam.intro(exam.id));
  };

  return (
    <Card className="width-full">
      <CardHeader className="flex gap-3">
        <Image alt="avatar url" height={40} radius="sm" src={exam.author.avatarUrl} width={40} />
        <div className="flex flex-col">
          <p className="text-md">{exam.author.name}</p>
          <p className="text-small text-default-500">{fromNowDate(exam.createdAt)}</p>
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
        <CardDescription>{exam.description}</CardDescription>
      </CardBody>
      <Divider />
      <CardFooter className="flex justify-between">
        <div>문제 수: {exam.questionCount}</div>
        <Button size="sm" variant="shadow" onPress={handleExamStart}>
          시험 보기
        </Button>
      </CardFooter>
    </Card>
  );
};

export default ExamSummaryCard;
