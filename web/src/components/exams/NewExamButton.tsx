import { useNavigate } from 'react-router';
import useCreateExam from '@/hooks/api/exam/useCreateExam';
import {
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Button,
  useDisclosure,
  Input,
} from '@nextui-org/react';
import { useState } from 'react';
import { Routes } from '@/constants';

const NewExamButton = () => {
  const { isOpen, onOpen, onOpenChange, onClose } = useDisclosure();
  const [title, setTitle] = useState('');
  const { mutate } = useCreateExam();
  const navigate = useNavigate();

  const handleCreateNewExam = async () => {
    mutate(
      {
        title,
      },
      {
        onSuccess: (data) => {
          const examId = data.id;
          navigate(Routes.exam.management.questions(examId));
        },
        onSettled: () => {
          onClose();
        },
      },
    );
  };

  return (
    <div>
      <Button color="primary" variant="shadow" onClick={onOpen}>
        새로운 시험 생성
      </Button>
      <Modal isOpen={isOpen} onOpenChange={onOpenChange}>
        <ModalContent>
          {(onClose) => (
            <>
              <ModalHeader className="flex flex-col gap-1">새로운 시험 생성</ModalHeader>
              <ModalBody>
                <Input label="시험 제목" value={title} onValueChange={setTitle} />
              </ModalBody>
              <ModalFooter>
                <Button color="danger" variant="light" onPress={onClose}>
                  닫기
                </Button>
                <Button color="primary" onPress={handleCreateNewExam}>
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

export default NewExamButton;
