import { Outlet } from 'react-router';
import ExamProgressHeader from './ExamProgressHeader';

/**
 * 모바일 주소창을 제외한 화면의 높이로 설정하기 위해 100vh 대신 100svh를 사용합니다.
 * 헤더를 제외한 공간을 채우기 위해 65px(Header Height)을 뺀 100svh를 사용합니다.
 */
const ExamProgressLayout = () => {
  return (
    <div className="flex flex-col 100svh w-screen bg-white">
      <ExamProgressHeader />
      <div className="h-[calc(100svh-65px)] w-full overflow-y-auto pb-14 pt-6 bg-gray-100">
        <main className="h-full w-full overflow-y-auto bg-white">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default ExamProgressLayout;
