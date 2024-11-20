import { useNavigate, useParams } from 'react-router-dom';
import BaseLayout from '@/components/layouts/base/BaseLayout.tsx';
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
      <div className="flex h-full bg-gray-100">
        <ExamEditorSidebar />
        <main className="flex-1 p-6 bg-white overflow-y-auto">
          <h1 className="text-2xl font-bold text-gray-800 mb-4 flex items-center justify-between">
            시험({id})
            <button
              onClick={handlePublish}
              className="ml-2 px-2 py-1 text-medium bg-green-600 text-white rounded-md hover:bg-green-700 transition duration-200 shadow-md"
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
