import useGetExamWithAnswers from '@/hooks/api/exam/useGetExamWithAnswers';
import EditableInput from './EditableInput';
import useUpdateExamTitle from '@/hooks/api/exam/useUpdateExamTitle';
import { useQueryClient } from '@tanstack/react-query';

interface ExamTitleOverviewProps {
  examId: number;
  title: string;
  isPublished: boolean;
}

const ExamTitleOverview = ({ examId, title, isPublished }: ExamTitleOverviewProps) => {
  const queryclient = useQueryClient();

  const { mutate: updateTitleMutate } = useUpdateExamTitle();

  const handleUpdateTitle = (title: string) => {
    updateTitleMutate(
      { examId, request: { title } },
      {
        onSuccess: () => {
          queryclient.invalidateQueries({
            queryKey: useGetExamWithAnswers.getKey(examId),
          });
        },
      },
    );
  };

  return (
    <>
      <div className="font-semibold text-2xl">시험 제목</div>
      {isPublished ? (
        <div className="text-gray-500">{title}</div>
      ) : (
        <EditableInput initialValue={title} onSave={handleUpdateTitle} maxLength={100} />
      )}
    </>
  );
};

export default ExamTitleOverview;
