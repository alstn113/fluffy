import { useNavigate } from 'react-router-dom';
import useCreateExam from '@/hooks/api/exam/useCreateExam';
import { Button } from '@nextui-org/react';

const NewExamButton = () => {
  const { mutate } = useCreateExam();
  const navigate = useNavigate();

  const handleCreateNewExam = async () => {
    mutate(
      {
        title: 'New Exam',
      },
      {
        onSuccess: (data) => {
          const examId = data.id;
          navigate(`/exams/${examId}/edit`);
        },
      },
    );
  };

  return (
    <div>
      <Button onClick={handleCreateNewExam}>Create New Exam</Button>
    </div>
  );
};

export default NewExamButton;
