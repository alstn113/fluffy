import useExamEditorStore from '@/stores/useExamEditorStore';
import { SingleChoiceQuestionRequest } from '@/api/questionAPI';
import { Radio, RadioGroup } from '@nextui-org/radio';

const SingleChoiceQuestionView = () => {
  const { currentIndex, questions } = useExamEditorStore();
  const question = questions[currentIndex] as SingleChoiceQuestionRequest;

  return (
    <div className="flex flex-col mt-8">
      <label className="mb-2 font-semibold">선택지와 정답</label>
      <RadioGroup value={question.options.findIndex((option) => option.isCorrect).toString()}>
        <div>
          {question.options.map((option, index) => (
            <div key={index} className="flex items-center p-2 rounded-md mb-2 hover:bg-gray-100">
              <Radio color="secondary" value={index.toString()} disableAnimation />
              <input
                readOnly
                type="text"
                value={option.text}
                className="p-2 border-b border-gray-300 rounded-none ml-2 bg-transparent focus:outline-none min-w-[400px]"
              />
            </div>
          ))}
        </div>
      </RadioGroup>
    </div>
  );
};

export default SingleChoiceQuestionView;
