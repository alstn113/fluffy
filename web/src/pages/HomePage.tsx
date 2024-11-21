import BaseLayout from '@/components/layouts/base/BaseLayout.tsx';
import NewExamButton from '@/components/exams/NewExamButton';

const HomePage = () => {
  return (
    <BaseLayout>
      <div className="flex flex-col justify-center items-start gap-4 mx-auto my-8">
        <h1 className="text-2xl font-bold">Home Page</h1>
        <NewExamButton />
      </div>
    </BaseLayout>
  );
};

export default HomePage;
