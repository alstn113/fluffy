import Header from '@/components/layouts/base/Header.tsx';

interface BaseLayoutProps {
  children: React.ReactNode;
}

const BaseLayout = ({ children }: BaseLayoutProps) => {
  return (
    <div className="relative flex flex-col h-screen">
      <Header />
      <main className="flex flex-col flex-1 w-full">{children}</main>
    </div>
  );
};

export default BaseLayout;
