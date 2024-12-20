import useSubmissionStore from '@/stores/useSubmissionStore';
import { Button } from '@nextui-org/react';

const MoveQuestionButtonGroup = () => {
  const { currentQuestionIndex, questionLength, moveNext, movePrev } = useSubmissionStore();

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
          color="primary"
          variant="shadow"
          onPress={moveNext}
          isDisabled={currentQuestionIndex === questionLength - 1}
        >
          다음
        </Button>
      </div>
    </div>
  );
};

export default MoveQuestionButtonGroup;
