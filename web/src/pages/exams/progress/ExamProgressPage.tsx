import { useEffect } from 'react';
import { useParams } from 'react-router';
import useGetExam from '@/hooks/api/exam/useGetExam.ts';
import QuestionDetailTemplate from '@/components/questions/details/QuestionDetailTemplate.tsx';
import useSubmissionStore from '@/stores/useSubmissionStore.ts';
import AsyncBoundary from '@/components/AsyncBoundary.tsx';
import ExamProgressBar from '@/components/exams/ExamProgressBar';
import MoveQuestionButtonGroup from '@/components/exams/MoveQuestionButtonGroup';
import useBeforeUnload from '@/hooks/useBeforeUnload';

const ExamProgressPage = () => {
  const params = useParams() as { examId: string };
  const examId = Number(params.examId);

  useBeforeUnload();

  return (
    <div className="w-full h-full flex items-center flex-col gap-4">
      <div className="w-full flex flex-col max-w-2xl py-8 px-4">
        <AsyncBoundary>
          <ExamProgressContent examId={examId} />
        </AsyncBoundary>
      </div>
    </div>
  );
};

const ExamProgressContent = ({ examId }: { examId: number }) => {
  const { data } = useGetExam(examId);
  const { initialize, currentQuestionIndex } = useSubmissionStore();

  useEffect(() => {
    if (data && data.questions) {
      initialize(data.questions.length);
    }
  }, [data, initialize]);

  return (
    <div className="w-full flex flex-col justify-center items-start gap-4 mx-auto">
      <ExamProgressBar />
      <QuestionDetailTemplate
        question={data.questions[currentQuestionIndex]}
        index={currentQuestionIndex}
      />
      <MoveQuestionButtonGroup />
    </div>
  );
};

export default ExamProgressPage;
