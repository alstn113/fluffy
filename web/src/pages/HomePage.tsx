import BaseLayout from '@/components/layouts/BaseLayout';
import NewExamButton from '@/components/exams/NewExamButton';
import QuestionDetailTemplate from '@/components/questions/details/QuestionDetailTemplate';
import { questions } from './questions';

const HomePage = () => {
  return (
    <BaseLayout>
      <div className="flex flex-col justify-center items-start gap-4 mx-auto my-8">
        <h1 className="text-2xl font-bold">Home Page</h1>
        <NewExamButton />

        <div className="grid grid-cols-2 gap-4">
          {questions.map((question, index) => {
            return <QuestionDetailTemplate key={question.id} question={question} index={index} />;
          })}
        </div>
      </div>
    </BaseLayout>
  );
};

export default HomePage;
