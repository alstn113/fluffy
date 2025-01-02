import { Routes } from '@/constants';
import useCreateSubmission from '@/hooks/api/submission/useCreateSubmission';
import useSubmissionStore from '@/stores/useSubmissionStore';
import {
  Button,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  useDisclosure,
} from '@nextui-org/react';
import { BsCloudArrowUpFill } from 'react-icons/bs';
import { useNavigate, useParams } from 'react-router';

const MoveQuestionButtonGroup = () => {
  const { currentQuestionIndex, questionLength, moveNext, movePrev } = useSubmissionStore();
  const { isOpen, onOpen, onOpenChange, onClose } = useDisclosure();
  const params = useParams() as { examId: string };
  const examId = Number(params.examId);
  const navigate = useNavigate();
  const { mutate } = useCreateSubmission();
  const { questionResponses } = useSubmissionStore();

  const handleSubmit = () => {
    mutate(
      { examId, request: { questionResponses: questionResponses } },
      {
        onSuccess: () => {
          navigate(Routes.exam.submissions(examId));
        },
        onSettled: () => {
          onClose();
        },
      },
    );
  };

  return (
    <div className="fixed left-0 bottom-32 w-full flex justify-center items-center">
      <div className="flex w-full justify-center items-center gap-4">
        <Button
          color="primary"
          variant="shadow"
          onPress={movePrev}
          isDisabled={currentQuestionIndex === 0}
        >
          이전
        </Button>
        <Button
          className="text-white"
          color="success"
          variant="shadow"
          onPress={onOpen}
          startContent={<BsCloudArrowUpFill size={24} />}
        >
          제출
        </Button>
        <Button
          color="primary"
          variant="shadow"
          onPress={moveNext}
          isDisabled={currentQuestionIndex === questionLength - 1}
        >
          다음
        </Button>
        <Modal backdrop="blur" isOpen={isOpen} onOpenChange={onOpenChange}>
          <ModalContent>
            {(onClose) => (
              <>
                <ModalHeader className="flex flex-col gap-1">정말 제출하시겠습니까?</ModalHeader>
                <ModalBody>
                  <p>제출한 시험은 수정할 수 없습니다.</p>
                  <p>제출 후 대시보드에서 결과를 확인할 수 있습니다.</p>
                </ModalBody>
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
    </div>
  );
};

export default MoveQuestionButtonGroup;
