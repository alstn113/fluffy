import BaseLayout from '@/components/layouts/base/BaseLayout.tsx';
import AsyncBoundary from '@/components/AsyncBoundary';
import useGetMyExamSummaries from '@/hooks/api/exam/useGetMyExamSummaries';
import DraftExamCard from '@/components/dashboard/DraftExamCard';
import { Divider } from '@nextui-org/react';

const DashboardPage = () => {
  return (
    <BaseLayout>
      <div className="container mx-auto px-5 py-16">
        <AsyncBoundary>
          <div className="text-2xl font-semibold mb-5">출제 준비 중인 시험들</div>
          <DraftExamContent />
        </AsyncBoundary>
        <Divider className="my-10" />
        <AsyncBoundary>
          <div className="text-2xl font-semibold mb-5">출제 완료된 시험들</div>
          <PublishedExamContent />
        </AsyncBoundary>
      </div>
    </BaseLayout>
  );
};

const DraftExamContent = () => {
  const { data } = useGetMyExamSummaries('DRAFT');

  if (!data || data.length === 0) {
    return (
      <div className="w-full text-center text-xl text-gray-500">
        출제 준비 중인 시험이 없습니다.
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
      {data?.map((exam) => {
        return <DraftExamCard exam={exam} />;
      })}
    </div>
  );
};

const PublishedExamContent = () => {
  const { data } = useGetMyExamSummaries('PUBLISHED');

  if (!data || data.length === 0) {
    return (
      <div className="w-full text-center text-xl text-gray-500">출제 완료된 시험이 없습니다.</div>
    );
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
      {data?.map((exam) => {
        return <DraftExamCard exam={exam} />;
      })}
    </div>
  );
};

export default DashboardPage;
