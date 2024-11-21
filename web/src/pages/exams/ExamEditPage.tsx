import { useNavigate, useParams } from 'react-router-dom';
import ExamEditorSidebar from '@/components/questions/editors/ExamEditorSidebar.tsx';
import QuestionEditorTemplate from '@/components/questions/editors/QuestionEditorTemplate';
import QuestionTypeSelector from '@/components/questions/editors/QuestionTypeSelector';
import useExamEditorStore from '@/stores/useExamEditorStore';
import usePublishExam from '@/hooks/api/exam/usePublishExam';
import EditorLayout from '@/components/layouts/editor/EditorLayout';

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
    <EditorLayout>
      <div className="flex flex-row overflow-y-auto h-full w-full">
        <ExamEditorSidebar />
        <div className="flex grow flex-col h-full p-6 overflow-y-auto items-center">
          <div className="flex w-full max-w-[650px] flex-col gap-4">
            <h1 className="text-2xl font-bold text-gray-800 mb-4 flex items-center justify-between">
              시험 ({id})
              <button
                onClick={handlePublish}
                className="ml-2 px-2 py-1 text-medium bg-green-600 text-white rounded-md w-20 hover:bg-green-700 transition duration-200 shadow-md"
              >
                제출
              </button>
            </h1>
            {questionTypeSelectorActive ? <QuestionTypeSelector /> : <QuestionEditorTemplate />}
          </div>
        </div>
      </div>
    </EditorLayout>
  );
};

export default ExamEditPage;
