import AsyncBoundary from '@/components/AsyncBoundary';
import useGetMyExamSummaries from '@/hooks/api/exam/useGetMyExamSummaries';
import DraftExamCard from '@/components/dashboard/DraftExamCard';
import PublishedExamCard from '@/components/dashboard/PublishedExamCard';
import { Pagination, Tab, Tabs } from '@nextui-org/react';
import { BsFillFileEarmarkCheckFill } from 'react-icons/bs';
import { PiPencilLineBold } from 'react-icons/pi';
import NewExamButton from '@/components/exams/NewExamButton';
import { useState } from 'react';

const DashboardPage = () => {
  return (
    <div className="container mx-auto px-5 py-12">
      <div className="flex items-center justify-between mb-4">
        <div>
          <h1 className="text-4xl font-bold">Dashboard</h1>
          <p className="text-lg text-gray-500">Manage your exams here</p>
        </div>
        <NewExamButton />
      </div>
      <div className="flex w-full flex-col">
        <Tabs
          aria-label="Options"
          color="primary"
          variant="underlined"
          classNames={{
            tabList: 'gap-6 w-full relative rounded-none p-0 border-b border-divider',
            cursor: 'w-full bg-[#17C964]',
            tab: 'max-w-fit px-0 h-12',
            tabContent: 'group-data-[selected=true]:text-[#17C964]',
          }}
        >
          <Tab
            key="draft"
            title={
              <div className="flex items-center space-x-2">
                <PiPencilLineBold size={20} />
                <span>출제 준비</span>
              </div>
            }
          >
            <AsyncBoundary>
              <div className="text-2xl font-semibold mb-5">출제 준비 중인 시험들</div>
              <DraftExamContent />
            </AsyncBoundary>
          </Tab>
          <Tab
            key="published"
            title={
              <div className="flex items-center space-x-2">
                <BsFillFileEarmarkCheckFill size={20} />
                <span>출제 완료</span>
              </div>
            }
          >
            <AsyncBoundary>
              <div className="text-2xl font-semibold mb-5">출제 완료된 시험들</div>
              <PublishedExamContent />
            </AsyncBoundary>
          </Tab>
        </Tabs>
      </div>
    </div>
  );
};

const DraftExamContent = () => {
  const [page, setPage] = useState(1);
  const { data } = useGetMyExamSummaries('DRAFT', page - 1);
  const { content, pageInfo } = data;

  if (!content || pageInfo.totalElements === 0) {
    return (
      <div className="w-full text-center text-xl text-gray-500 mt-24">
        출제 준비 중인 시험이 없습니다.
      </div>
    );
  }

  return (
    <>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 2xl:grid-cols-4 gap-5">
        {content.map((exam) => {
          return <DraftExamCard key={exam.id} exam={exam} />;
        })}
      </div>
      <div className="flex justify-center mt-10">
        <Pagination
          color="primary"
          showControls
          showShadow
          page={page}
          total={pageInfo.totalPages}
          onChange={(page) => setPage(page)}
        />
      </div>
    </>
  );
};

const PublishedExamContent = () => {
  const [page, setPage] = useState(1);
  const { data } = useGetMyExamSummaries('PUBLISHED', page - 1);
  const { content, pageInfo } = data;

  if (!content || pageInfo.totalElements === 0) {
    return (
      <div className="w-full text-center text-xl text-gray-500 mt-24">
        출제 완료된 시험이 없습니다.
      </div>
    );
  }

  return (
    <>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 2xl:grid-cols-4 gap-5">
        {content.map((exam) => {
          return <PublishedExamCard key={exam.id} exam={exam} />;
        })}
      </div>
      <div className="flex justify-center mt-10">
        <Pagination
          color="primary"
          showControls
          showShadow
          page={page}
          total={pageInfo.totalPages}
          onChange={(page) => setPage(page)}
        />
      </div>
    </>
  );
};

export default DashboardPage;
