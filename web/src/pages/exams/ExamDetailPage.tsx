import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router';
import useGetExam from '@/hooks/api/exam/useGetExam';
import QuestionDetailTemplate from '@/components/questions/details/QuestionDetailTemplate';
import useCreateSubmission from '@/hooks/api/submission/useCreateSubmission';
import {
  Button,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  useDisclosure,
} from '@nextui-org/react';
import useSubmissionStore from '@/stores/useSubmissionStore';
import { Routes } from '@/constants';
import AsyncBoundary from '@/components/AsyncBoundary';

const ExamDetailPage = () => {
  const params = useParams() as { examId: string };
  const examId = Number(params.examId);

  return (
    <AsyncBoundary>
      <ExamDetailContent examId={examId} />
    </AsyncBoundary>
  );
};

interface ExamDetailPageContentProps {
  examId: number;
}

const ExamDetailContent = ({ examId }: ExamDetailPageContentProps) => {
  const { data } = useGetExam(examId);
  const navigate = useNavigate();
  const { mutate } = useCreateSubmission();
  const { questionResponses, initialize } = useSubmissionStore();
  const { isOpen, onOpen, onOpenChange, onClose } = useDisclosure();

  useEffect(() => {
    if (data && data.questions) {
      initialize(data.questions.length);
    }
  }, [data, initialize]);

  const handleSubmit = () => {
    mutate(
      { examId: data.id, request: { questionResponses: questionResponses } },
      {
        onSuccess: () => {
          navigate(Routes.home());
        },
        onSettled: () => {
          onClose();
        },
      },
    );
  };

  const { title, description, questions } = data;

  return (
    <div className="flex flex-col justify-center items-start gap-4 mx-auto my-8">
      <h1 className="text-3xl font-bold">{title}</h1>
      <p className="text-lg">{description}</p>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 md:p-0 p-4">
        {questions.map((question, index) => {
          return <QuestionDetailTemplate key={question.id} question={question} index={index} />;
        })}
      </div>
      <Button
        className="self-end"
        onPress={onOpen}
        color="primary"
        variant="shadow"
      >
        제출하기
      </Button>
      <Modal backdrop="blur" isOpen={isOpen} onOpenChange={onOpenChange}>
        <ModalContent>
          {(onClose) => (
            <>
              <ModalHeader className="flex flex-col gap-1">정말 제출하시겠습니까?</ModalHeader>
              <ModalBody>제출 후 수정이 불가능합니다.</ModalBody>
              <ModalFooter>
                <Button color="danger" variant="light" onPress={onClose}>
                  닫기
                </Button>
                <Button color="primary" onPress={handleSubmit}>
                  확인
                </Button>
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
    </div>
  );
};

export default ExamDetailPage;
