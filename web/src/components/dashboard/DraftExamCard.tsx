import { MyExamSummaryResponse } from '@/api/examAPI';
import { Routes } from '@/constants';
import { fromNowDate } from '@/lib/date.ts';
import { Card, CardFooter, CardHeader, Chip, Divider, Image, Link, Tooltip } from '@heroui/react';
import BaseCardBody from '../common/BaseCardBody';
import { PiListChecksBold } from 'react-icons/pi';

interface DraftExamCardProps {
  exam: MyExamSummaryResponse;
}

const DraftExamCard = ({ exam }: DraftExamCardProps) => {
  return (
    <Card className="max-w-[400px]">
      <CardHeader className="flex gap-3">
        <Image alt="avatar url" height={40} radius="sm" src={exam.author.avatarUrl} width={40} />
        <div className="flex flex-col">
          <p className="text-md">{exam.author.name}</p>
          <p className="text-small text-default-500">생성 시간: {fromNowDate(exam.createdAt)}</p>
          <p className="text-small text-default-500">수정 시간: {fromNowDate(exam.updatedAt)}</p>
        </div>
        <div className="ml-auto mr-4 flex items-center">
          <Chip color="warning" variant="shadow" classNames={{ content: 'text-white' }}>
            출제 준비
          </Chip>
        </div>
      </CardHeader>
      <Divider />
      <BaseCardBody title={exam.title} description={exam.description} />
      <Divider />
      <CardFooter className="flex justify-between">
        <Tooltip content="문항 수" placement="top" showArrow color="foreground">
          <div className="flex gap-1 text-indigo-400 font-bold">
            <PiListChecksBold size={24} />
            {exam.questionCount}
          </div>
        </Tooltip>
        <Link href={Routes.exam.management.questions(exam.id)}>이어서 편집하기</Link>
      </CardFooter>
    </Card>
  );
};

export default DraftExamCard;
