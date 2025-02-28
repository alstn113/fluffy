import { Routes } from '@/constants';
import usePublishExam from '@/hooks/api/exam/usePublishExam';
import useExamEditorStore from '@/stores/useExamEditorStore';
import {
  Button,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  useDisclosure,
} from '@heroui/react';
import { useNavigate } from 'react-router';

interface ExamPublishButtonProps {
  examId: number;
}

const ExamPublishButton = ({ examId }: ExamPublishButtonProps) => {
  const { isOpen, onClose, onOpen } = useDisclosure();
  const { questions } = useExamEditorStore();
  const { mutate: publishExamMutate, isPending } = usePublishExam();
  const navigate = useNavigate();

  const handlePublishExam = () => {
    publishExamMutate(
      {
        examId,
        request: {
          questions,
        },
      },
      {
        onSuccess: () => {
          onClose();
          navigate(Routes.home());
        },
      },
    );
  };

  return (
    <>
      <Button color="success" variant="shadow" onPress={onOpen} className="text-white">
        시험 출제
      </Button>
      <Modal isOpen={isOpen} onClose={onClose}>
        <ModalContent>
          {(onClose) => (
            <>
              <ModalHeader className="flex flex-col gap-1">시험을 출제하시겠습니까?</ModalHeader>
              <ModalBody>
                <div className="text-gray-600">
                  <p>시험 출제 후에는 수정이 불가능합니다.</p>
                  <p>출제된 시험은 대시보드에서 확인할 수 있습니다.</p>
                </div>
              </ModalBody>
              <ModalFooter>
                <Button color="danger" variant="light" onPress={onClose}>
                  취소
                </Button>
                <Button color="primary" onPress={handlePublishExam} isLoading={isPending}>
                  확인
                </Button>
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
    </>
  );
};

export default ExamPublishButton;
