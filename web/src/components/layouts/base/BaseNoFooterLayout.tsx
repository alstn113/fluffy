import Header from '@/components/layouts/base/Header.tsx';
import { Outlet } from 'react-router';

const BaseNoFooterLayout = () => {
  return (
    <div className="relative flex flex-col h-screen">
      <Header />
      <div className="h-full w-full overflow-y-auto pb-14 pt-6 bg-gray-100">
        <main className="h-full w-full overflow-y-auto bg-white">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default BaseNoFooterLayout;
