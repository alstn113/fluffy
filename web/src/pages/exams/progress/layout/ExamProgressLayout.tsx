import { Outlet } from 'react-router';
import ExamProgressHeader from './ExamProgressHeader';

const ExamProgressLayout = () => {
  return (
    <div className="flex flex-col h-screen w-screen bg-white">
      <ExamProgressHeader />
      {/* 모바일 주소창 고려, 헤더 64px */}
      <div className="h-[calc(100vh-64px)] w-full overflow-y-auto pb-14 pt-6 bg-gray-100">
        <main className="h-full w-full overflow-y-auto bg-white">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default ExamProgressLayout;
