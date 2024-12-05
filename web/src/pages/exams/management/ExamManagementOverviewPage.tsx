import useExamEditorStore from '@/stores/useExamEditorStore.ts';
import usePublishExam from '@/hooks/api/exam/usePublishExam.ts';
import {
  Button,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  useDisclosure,
} from '@nextui-org/react';
import { useNavigate, useParams } from 'react-router';
import { Routes } from '@/constants';
import useGetExamWithAnswers from '@/hooks/api/exam/useGetExamWithAnswers';

const ExamManagementOverviewPage = () => {
  const params = useParams() as { examId: string };
  const examId = Number(params.examId);

  const { isOpen, onClose, onOpen } = useDisclosure();
  const { questions } = useExamEditorStore();
  const { mutate } = usePublishExam();
  const navigate = useNavigate();

  const handlePublishExam = () => {
    mutate(
      {
        examId,
        request: {
          questions,
          startAt: null,
          endAt: null,
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
      <div className="w-full h-full flex items-center flex-col gap-4">
        <div className="text-2xl font-semibold">Overview</div>
        <div>
          <Button color="success" variant="shadow" onPress={onOpen} className="text-white">
            시험 출제
          </Button>
        </div>
      </div>
      <Modal size={'md'} isOpen={isOpen} onClose={onClose}>
        <ModalContent>
          {(onClose) => (
            <>
              <ModalHeader className="flex flex-col gap-1">시험 출제</ModalHeader>
              <ModalBody>
                <p>시험 시작일을 비워둘 경우 현재 시간으로 설정됩니다.</p>
                <p>시험 종료일을 비월둘 경우 무한히 진행됩니다.</p>
              </ModalBody>
              <ModalFooter>
                <Button color="danger" variant="light" onPress={onClose}>
                  취소
                </Button>
                <Button color="primary" onPress={handlePublishExam}>
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

const ExamManagementOverviewContent = ({ examId }: { examId: number }) => {
  const { data } = useGetExamWithAnswers(examId);

  return (
    <div>
      <div>{data.title}</div>
      <div>{data.description}</div>
    </div>
  );
};

export default ExamManagementOverviewPage;
