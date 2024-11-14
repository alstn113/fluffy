import { useNavigate } from 'react-router-dom';
import useCreateExam from '~/hooks/api/exam/useCreateExam';

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
      <button onClick={handleCreateNewExam}>Create New Exam</button>
    </div>
  );
};

export default NewExamButton;
