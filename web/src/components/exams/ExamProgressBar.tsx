import useSubmissionStore from '@/stores/useSubmissionStore';
import { Progress } from '@nextui-org/react';

const ExamProgressBar = () => {
  const { questionLength, currentQuestionIndex } = useSubmissionStore();
  return (
    <Progress
      aria-label="Downloading..."
      label={`문제 ${currentQuestionIndex + 1} / ${questionLength} 번`}
      color="success"
      size="sm"
      maxValue={questionLength}
      value={currentQuestionIndex + 1}
    />
  );
};

export default ExamProgressBar;
