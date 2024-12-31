import useGetSubmissionSummaries from '@/hooks/api/submission/useGetSubmissionSummaries.ts';
import AsyncBoundary from '@/components/AsyncBoundary.tsx';
import { useNavigate, useParams, useSearchParams } from 'react-router';
import {
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
  User,
} from '@nextui-org/react';
import { Key, useCallback } from 'react';
import { SubmissionSummaryResponse } from '@/api/submissionAPI.ts';
import { fullDate } from '@/lib/date.ts';
import SubmissionDetails from '@/components/analytics/SubmissionDetails';

const ExamManagementAnalyticsPage = () => {
  const params = useParams() as { examId: string };
  const examId = Number(params.examId);

  return (
    <div className="w-full h-full flex items-center flex-col gap-4">
      <div className="max-w-[750px] w-full mt-8">
        <AsyncBoundary>
          <ExamManagementAnalyticsContent examId={examId} />
        </AsyncBoundary>
      </div>
    </div>
  );
};

const ExamManagementAnalyticsContent = ({ examId }: { examId: number }) => {
  const { data } = useGetSubmissionSummaries(examId);
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const submissionId = Number(searchParams.get('submissionId'));

  const columns = [
    {
      name: '이름',
      uid: 'name',
    },
    {
      name: '제출일',
      uid: 'submittedAt',
    },
  ];

  const renderCell = useCallback((data: SubmissionSummaryResponse, columnKey: Key) => {
    switch (columnKey) {
      case 'name':
        return (
          <User
            avatarProps={{ radius: 'lg', src: data.participant.avatarUrl }}
            description={data.participant.email}
            name={data.participant.name}
          >
            {data.participant.name}
          </User>
        );
      case 'submittedAt':
        return (
          <div className="flex flex-col">
            <p className="text-bold text-sm capitalize">{fullDate(data.submittedAt)}</p>
          </div>
        );
      default:
        return null;
    }
  }, []);

  if (submissionId) {
    return <SubmissionDetails examId={examId} />;
  }

  return (
    <Table aria-label="Example table with custom cells">
      <TableHeader columns={columns}>
        {(column) => <TableColumn key={column.uid}>{column.name}</TableColumn>}
      </TableHeader>
      <TableBody items={data} emptyContent={<div>아직 제출된 응답이 없습니다.</div>}>
        {(item) => (
          <TableRow
            key={item.id}
            className="cursor-pointer hover:bg-gray-100 transition-colors"
            onClick={() =>
              navigate(`/exams/${examId}/management/analytics?submissionId=${item.id}`)
            }
          >
            {(columnKey) => <TableCell>{renderCell(item, columnKey)}</TableCell>}
          </TableRow>
        )}
      </TableBody>
    </Table>
  );
};

export default ExamManagementAnalyticsPage;
