import { ExamSummaryResponse } from '@/api/examAPI';
import { Routes } from '@/constants';
import { fromNowDate } from '@/lib/date.ts';
import { Button, Card, CardFooter, CardHeader, Chip, Divider, Image, Tooltip } from '@heroui/react';
import { useNavigate } from 'react-router';
import BaseCardBody from '../common/BaseCardBody';
import { PiListChecksBold, PiThumbsUpBold } from 'react-icons/pi';

interface ExamSummaryCardProps {
  exam: ExamSummaryResponse;
}

const ExamSummaryCard = ({ exam }: ExamSummaryCardProps) => {
  const navigate = useNavigate();
  const handleExamStart = () => {
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
      <BaseCardBody title={exam.title} description={exam.description} />
      <Divider />
      <CardFooter className="flex justify-between">
        <div className="flex gap-2">
          <Tooltip content="문항 수" placement="top" showArrow color="foreground">
            <div className="flex gap-1 text-indigo-400 font-bold">
              <PiListChecksBold size={24} />
              {exam.questionCount}
            </div>
          </Tooltip>
          <Tooltip content="좋아요 수" placement="top" showArrow color="foreground">
            <div className="flex gap-1 text-rose-400 font-bold">
              <PiThumbsUpBold size={24} />
              {exam.likeCount}
            </div>
          </Tooltip>
        </div>
        <Button size="sm" variant="shadow" onPress={handleExamStart}>
          시험 보기
        </Button>
      </CardFooter>
    </Card>
  );
};

export default ExamSummaryCard;
