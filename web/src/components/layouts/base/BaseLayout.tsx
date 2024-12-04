import Header from '@/components/layouts/base/Header.tsx';
import { Outlet } from 'react-router';

const BaseLayout = () => {
  return (
    <div className="relative flex flex-col h-screen">
      <Header />
      <main className="flex flex-col flex-1 w-full">
        <Outlet/>
      </main>
    </div>
  );
};

export default BaseLayout;
