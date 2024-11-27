import BaseLayout from '@/components/layouts/base/BaseLayout';

const NotFoundPage = () => {
  return (
    <BaseLayout>
      <div className="w-full h-full flex justify-center items-center">
        <div className="text-2xl font-semibold">404 Not Found</div>
      </div>
    </BaseLayout>
  );
};

export default NotFoundPage;
