import { Routes } from '@/constants';
import usePublishExam from '@/hooks/api/exam/usePublishExam';
import useExamEditorStore from '@/stores/useExamEditorStore';
import {
  Button,
  DatePicker,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  Switch,
  useDisclosure,
} from '@nextui-org/react';
import { useState } from 'react';
import { useNavigate } from 'react-router';
import { now, getLocalTimeZone, ZonedDateTime } from '@internationalized/date';

interface ExamPublishButtonProps {
  examId: number;
}

const ExamPublishButton = ({ examId }: ExamPublishButtonProps) => {
  const { isOpen, onClose, onOpen } = useDisclosure();
  const { questions } = useExamEditorStore();
  const { mutate: publishExamMutate } = usePublishExam();
  const navigate = useNavigate();

  const [startAt, setStartAt] = useState(now(getLocalTimeZone()).add({ hours: 1 }));
  const [endAt, setEndAt] = useState(now(getLocalTimeZone()).add({ hours: 3 }));
  const [isStartAtNow, setIsStartAtNow] = useState(false);
  const [isEndAtInfinite, setIsEndAtInfinite] = useState(false);

  const parseDate = (date: ZonedDateTime) => {
    return date.toString().substring(0, 16);
  };

  const validatePeriod = () => {
    if (!isStartAtNow && !startAt) return false;
    if (!isEndAtInfinite && !endAt) return false;

    const parsedStartAt = isStartAtNow ? null : parseDate(startAt);
    const parsedEndAt = isEndAtInfinite ? null : parseDate(endAt);

    if (parsedStartAt && parsedEndAt && parsedStartAt >= parsedEndAt) return false;
    if (parsedStartAt && parsedStartAt < now(getLocalTimeZone()).toString().substring(0, 16)) return false;

    return true;
  };

  const handlePublishExam = () => {
    if (!validatePeriod()) return;

    publishExamMutate(
      {
        examId,
        request: {
          questions,
          startAt: isStartAtNow ? null : parseDate(startAt),
          endAt: isEndAtInfinite ? null : parseDate(endAt),
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
      <Modal size={'2xl'} isOpen={isOpen} onClose={onClose}>
        <ModalContent>
          {(onClose) => (
            <>
              <ModalHeader className="flex flex-col gap-1">시험 출제할 날짜와 시간을 선택하세요</ModalHeader>
              <ModalBody>
                <div className="flex gap-4 w-full justify-evenly">
                  <div className="flex flex-col gap-4">
                    <p>시험 시작일을 설정합니다.</p>
                    <DatePicker
                      label="시작일"
                      variant="bordered"
                      granularity="minute"
                      hideTimeZone
                      value={startAt}
                      onChange={setStartAt}
                      isDisabled={isStartAtNow}
                    />
                    <Switch checked={isStartAtNow} onValueChange={setIsStartAtNow}>
                      시험 시작일을 현재 시간으로 설정
                    </Switch>
                  </div>
                  <div className="flex flex-col gap-4">
                    <p>시험 종료일을 설정합니다.</p>
                    <DatePicker
                      label="종료일"
                      variant="bordered"
                      granularity="minute"
                      hideTimeZone
                      value={endAt}
                      onChange={setEndAt}
                      isDisabled={isEndAtInfinite}
                    />
                    <Switch checked={isEndAtInfinite} onValueChange={setIsEndAtInfinite}>
                      시험 종료일을 무기한으로 설정
                    </Switch>
                  </div>
                </div>
              </ModalBody>
              <ModalFooter>
                <Button color="danger" variant="light" onPress={onClose}>
                  취소
                </Button>
                <Button color="primary" onPress={handlePublishExam} isDisabled={!validatePeriod()}>
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
