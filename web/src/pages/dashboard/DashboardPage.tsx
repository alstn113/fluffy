import AsyncBoundary from '@/components/AsyncBoundary';
import { Tab, Tabs } from '@heroui/react';
import { BsFillFileEarmarkCheckFill, BsFillSendCheckFill } from 'react-icons/bs';
import { PiPencilLineBold } from 'react-icons/pi';
import NewExamButton from '@/components/exams/NewExamButton';
import DraftExamContent from '@/components/dashboard/DraftExamContent';
import PublishedExamContent from '@/components/dashboard/PublishedExamContent';
import SubmittedExamContent from '@/components/dashboard/SubmittedExamContent';

const DashboardPage = () => {
  return (
    <div className="container mx-auto px-5 py-12">
      <div className="flex items-center justify-between mb-4">
        <div>
          <h1 className="text-4xl font-bold">대시보드</h1>
          <p className="text-lg text-gray-500">시험들을 생성하고 관리하세요!</p>
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
          <Tab
            key="submissions"
            title={
              <div className="flex items-center space-x-2">
                <BsFillSendCheckFill size={20} />
                <span>제출한 시험</span>
              </div>
            }
          >
            <AsyncBoundary>
              <div className="text-2xl font-semibold mb-5">제출한 시험들</div>
              <SubmittedExamContent />
            </AsyncBoundary>
          </Tab>
        </Tabs>
      </div>
    </div>
  );
};

export default DashboardPage;
