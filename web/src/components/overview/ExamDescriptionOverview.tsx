import useUpdateExamDescription from '@/hooks/api/exam/useUpdateExamDescription';
import EditableInput from './EditableInput';

interface ExamDescriptionOverviewProps {
  isPublished: boolean;
  examId: number;
  description: string;
}

const ExamDescriptionOverview = ({
  isPublished,
  examId,
  description,
}: ExamDescriptionOverviewProps) => {
  const { mutate: updateDescriptionMutate } = useUpdateExamDescription();

  const handleUpdateDescription = (description: string) => {
    updateDescriptionMutate({ examId, request: { description } });
  };

  return (
    <>
      <div className="font-semibold">시험 설명</div>
      {isPublished ? (
        <div className="text-gray-500">{description}</div>
      ) : (
        <EditableInput
          initialValue={description}
          onSave={handleUpdateDescription}
          placeholder="시험 설명을 입력할 수 있습니다."
          isTextarea
          maxLength={2000}
        />
      )}
    </>
  );
};

export default ExamDescriptionOverview;
