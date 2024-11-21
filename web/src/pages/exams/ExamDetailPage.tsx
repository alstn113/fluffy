import { Suspense } from 'react';
import { useParams } from 'react-router-dom';
import BaseLayout from '@/components/layouts/base/BaseLayout.tsx';
import useGetExam from '@/hooks/api/exam/useGetExam';
import QuestionDetailTemplate from '@/components/questions/details/QuestionDetailTemplate';
import useCreateSubmission from '@/hooks/api/submission/useCreateSubmission';
import { Button } from '@nextui-org/react';
import useSubmissionStore from '@/stores/useSubmissionStore';

const ExamDetailPage = () => {
  const { id } = useParams() as { id: string };

  return (
    <BaseLayout>
      <Suspense fallback={<div>Loading...</div>}>
        <ExamDetailPageContent examId={id} />
      </Suspense>
    </BaseLayout>
  );
};

const ExamDetailPageContent = ({ examId }: { examId: string }) => {
  const { data } = useGetExam(examId);
  const { mutate } = useCreateSubmission();
  const { answers } = useSubmissionStore();

  const handleSubmit = () => {
    mutate({ examId: data.id, request: { answers } });
  };

  const { title, description, questions } = data;
  return (
    <div className="flex flex-col justify-center items-start gap-4 mx-auto my-8">
      <h1>{title}</h1>
      <p>{description}</p>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 md:p-0 p-4">
        {questions.map((question, index) => {
          return <QuestionDetailTemplate key={question.id} question={question} index={index} />;
        })}
      </div>
      {/* 오른쪽 정렬 */}
      <Button className="self-end" onClick={handleSubmit} color="secondary">
        제출하기
      </Button>
    </div>
  );
};

export default ExamDetailPage;
