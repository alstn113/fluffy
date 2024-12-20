import Header from '@/components/layouts/base/Header.tsx';
import { Outlet } from 'react-router';
import Footer from './Footer';

const BaseLayout = () => {
  return (
    <div className="relative flex flex-col h-screen">
      <Header />
      <main className="flex flex-col flex-1 w-full">
        <Outlet />
      </main>
      <Footer />
    </div>
  );
};

export default BaseLayout;
