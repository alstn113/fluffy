import NavbarLogo from '@/components/layouts/base/NavbarLogo';
import { Navbar, NavbarContent } from '@nextui-org/react';
import { FaArrowLeft } from 'react-icons/fa';
import { BsCloudArrowUpFill } from 'react-icons/bs';
import { Routes } from '@/constants';
import { useNavigate, useParams } from 'react-router';
import {
  Button,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  useDisclosure,
} from '@nextui-org/react';
import useCreateSubmission from '@/hooks/api/submission/useCreateSubmission';
import useSubmissionStore from '@/stores/useSubmissionStore';
import { Link } from 'react-router';

const ExamProgressHeader = () => {
  const params = useParams() as { examId: string };
  const examId = Number(params.examId);
  const navigate = useNavigate();
  const { mutate } = useCreateSubmission();
  const { questionResponses } = useSubmissionStore();
  const { isOpen, onOpen, onOpenChange, onClose } = useDisclosure();

  const handleSubmit = () => {
    mutate(
      { examId, request: { questionResponses: questionResponses } },
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

  return (
    <>
      <Navbar isBordered>
        <NavbarContent justify="start">
          <Link to={Routes.home()} className="flex items-center gap-2">
            <FaArrowLeft /> 종료
          </Link>
        </NavbarContent>
        <NavbarContent justify="center">
          <NavbarLogo />
        </NavbarContent>
        <NavbarContent justify="end">
          <Button
            color="default"
            variant="light"
            onPress={onOpen}
            startContent={<BsCloudArrowUpFill size={24} />}
          >
            제출
          </Button>
        </NavbarContent>
      </Navbar>
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
    </>
  );
};

export default ExamProgressHeader;
