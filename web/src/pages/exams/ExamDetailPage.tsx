import { Suspense, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import BaseLayout from '@/components/layouts/base/BaseLayout.tsx';
import useGetExam from '@/hooks/api/exam/useGetExam';
import QuestionDetailTemplate from '@/components/questions/details/QuestionDetailTemplate';
import useCreateSubmission from '@/hooks/api/submission/useCreateSubmission';
import { Button } from '@nextui-org/react';
import useSubmissionStore from '@/stores/useSubmissionStore';
import { PAGE_LIST } from '@/constants';

const ExamDetailPage = () => {
  const { id } = useParams();
  const examId = Number(id);

  return (
    <BaseLayout>
      <Suspense fallback={<div>Loading...</div>}>
        <ExamDetailPageContent examId={examId} />
      </Suspense>
    </BaseLayout>
  );
};

interface ExamDetailPageContentProps {
  examId: number;
}

const ExamDetailPageContent = ({ examId }: ExamDetailPageContentProps) => {
  const { data } = useGetExam(examId);
  const navigate = useNavigate();
  const { mutate } = useCreateSubmission();
  const { answers, initialize } = useSubmissionStore();

  useEffect(() => {
    if (data && data.questions) {
      initialize(data.questions.length);
    }
  }, [data, initialize]);

  const handleSubmit = () => {
    mutate(
      { examId: data.id, request: { answers } },
      {
        onSuccess: () => {
          navigate(PAGE_LIST.home);
        },
      },
    );
  };

  const { title, description, questions } = data;

  return (
    <div className="flex flex-col justify-center items-start gap-4 mx-auto my-8">
      <h1 className="text-3xl font-bold">{title}</h1>
      <p className="text-lg">{description}</p>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 md:p-0 p-4">
        {questions.map((question, index) => {
          return <QuestionDetailTemplate key={question.id} question={question} index={index} />;
        })}
      </div>
      <Button className="self-end" onClick={handleSubmit} color="secondary">
        제출하기
      </Button>
    </div>
  );
};

export default ExamDetailPage;
