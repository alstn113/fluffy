import { useNavigate, useParams } from 'react-router-dom';
import BaseLayout from '@/components/layouts/BaseLayout';
import ExamEditorSidebar from '@/components/questions/editors/ExamEditorSidebar.tsx';
import QuestionEditorTemplate from '@/components/questions/editors/QuestionEditorTemplate';
import QuestionTypeSelector from '@/components/questions/editors/QuestionTypeSelector';
import useExamEditorStore from '@/stores/useExamEditorStore';
import usePublishExam from '@/hooks/api/exam/usePublishExam';

const ExamEditPage = () => {
  const { id } = useParams() as { id: string };
  const { questionTypeSelectorActive, questions } = useExamEditorStore();
  const { mutate } = usePublishExam();
  const navigate = useNavigate();

  const handlePublish = () => {
    mutate(
      {
        examId: id,
        request: {
          questions,
        },
      },
      {
        onSuccess: () => {
          navigate(`/`);
        },
      },
    );
  };

  return (
    <BaseLayout>
      <div className="flex h-full">
        <ExamEditorSidebar />
        <main className="flex-1 p-4 bg-white overflow-y-auto">
          <h1 className="text-2xl mb-2">
            ExamEditPage {id}
            <button
              onClick={handlePublish}
              className="ml-4 px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition duration-200"
            >
              제출
            </button>
          </h1>

          {questionTypeSelectorActive ? <QuestionTypeSelector /> : <QuestionEditorTemplate />}
        </main>
      </div>
    </BaseLayout>
  );
};

export default ExamEditPage;
