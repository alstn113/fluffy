import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router';
import useGetExam from '@/hooks/api/exam/useGetExam.ts';
import QuestionDetailTemplate from '@/components/questions/details/QuestionDetailTemplate.tsx';
import useCreateSubmission from '@/hooks/api/submission/useCreateSubmission.ts';
import {
  Button,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  Progress,
  useDisclosure,
} from '@nextui-org/react';
import useSubmissionStore from '@/stores/useSubmissionStore.ts';
import { Routes } from '@/constants';
import AsyncBoundary from '@/components/AsyncBoundary.tsx';

const ExamProgressPage = () => {
  const params = useParams() as { examId: string };
  const examId = Number(params.examId);

  return (
    <AsyncBoundary>
      <ExamProgressContent examId={examId} />
    </AsyncBoundary>
  );
};

interface ExamProgressContentProps {
  examId: number;
}

const ExamProgressContent = ({ examId }: ExamProgressContentProps) => {
  const [number, setNumber] = useState(0);
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
      <Progress
        aria-label="Downloading..."
        label={`문제 ${number + 1} / ${questions.length}`}
        color="success"
        size="md"
        maxValue={questions.length - 1}
        value={number}
      />
      <QuestionDetailTemplate question={questions[number]} index={number} />

      <div className="flex w-full justify-center items-center gap-4">
        <Button
          color="primary"
          variant="shadow"
          onPress={() => {
            if (number > 0) {
              setNumber(number - 1);
            }
          }}
          isDisabled={number === 0}
        >
          이전
        </Button>
        <Button
          color="primary"
          variant="shadow"
          onPress={() => {
            if (number < questions.length - 1) {
              setNumber(number + 1);
            }
          }}
          isDisabled={number === questions.length - 1}
        >
          다음
        </Button>
      </div>
      <Button className="self-end" onPress={onOpen} color="primary" variant="shadow">
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

export default ExamProgressPage;
