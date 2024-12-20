import { useEffect } from 'react';
import { useParams } from 'react-router';
import useGetExam from '@/hooks/api/exam/useGetExam.ts';
import QuestionDetailTemplate from '@/components/questions/details/QuestionDetailTemplate.tsx';
import useSubmissionStore from '@/stores/useSubmissionStore.ts';
import AsyncBoundary from '@/components/AsyncBoundary.tsx';
import ExamProgressBar from '@/components/exams/ExamProgressBar';
import MoveQuestionButtonGroup from '@/components/exams/MoveQuestionButtonGroup';
import useBeforeUnload from '@/hooks/useBeforeUnload';
import { AnimatePresence, motion } from 'framer-motion';

const ExamProgressPage = () => {
  const params = useParams() as { examId: string };
  const examId = Number(params.examId);

  useBeforeUnload();

  return (
    <div className="w-full h-full flex items-center flex-col gap-4">
      <div className="w-full flex flex-col max-w-2xl p-8">
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
      <AnimatePresence mode="wait">
        <motion.div
          key={currentQuestionIndex}
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          exit={{ opacity: 0, y: -10 }}
          transition={{ duration: 0.2 }}
        >
          <QuestionDetailTemplate
            question={data.questions[currentQuestionIndex]}
            index={currentQuestionIndex}
          />
        </motion.div>
      </AnimatePresence>
      <MoveQuestionButtonGroup />
    </div>
  );
};

export default ExamProgressPage;
