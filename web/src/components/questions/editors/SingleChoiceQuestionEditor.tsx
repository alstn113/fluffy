import useExamEditorStore from '@/stores/useExamEditorStore';
import { SingleChoiceQuestionRequest } from '@/api/questionAPI';
import { Radio, RadioGroup } from '@nextui-org/radio';
import { DragDropContext, Draggable, Droppable, DropResult } from '@hello-pangea/dnd';

const SingleChoiceQuestionEditor = () => {
  const { handleUpdateQuestion, currentIndex, questions } = useExamEditorStore();
  const question = questions[currentIndex] as SingleChoiceQuestionRequest;

  const handleAddOption = () => {
    const newOption = { text: `옵션 ${question.options.length + 1}`, isCorrect: false };
    const newQuestion: SingleChoiceQuestionRequest = {
      ...question,
      options: [...question.options, newOption],
    };
    handleUpdateQuestion(currentIndex, newQuestion);
  };

  const handleRemoveOption = (index: number) => {
    const option = question.options[index];
    const isCorrectOption = option.isCorrect;

    const newOptions = question.options.filter((_, i) => i !== index);
    if (isCorrectOption && newOptions.length > 0) {
      newOptions[0] = { ...newOptions[0], isCorrect: true };
    }

    const newQuestion: SingleChoiceQuestionRequest = { ...question, options: newOptions };
    handleUpdateQuestion(currentIndex, newQuestion);
  };

  const handleUpdateOptionText = (index: number, text: string) => {
    const newOptions = question.options.map((option, i) =>
      i === index ? { ...option, text } : option,
    );
    const newQuestion: SingleChoiceQuestionRequest = { ...question, options: newOptions };
    handleUpdateQuestion(currentIndex, newQuestion);
  };

  const handleUpdateOptionCorrect = (index: number) => {
    const newOptions = question.options.map((option, i) =>
      i === index ? { ...option, isCorrect: true } : { ...option, isCorrect: false },
    );
    const newQuestion: SingleChoiceQuestionRequest = { ...question, options: newOptions };
    handleUpdateQuestion(currentIndex, newQuestion);
  };

  const handleMoveOption = (sourceIndex: number, destinationIndex: number) => {
    const newOptions = [...question.options];
    const [removedOption] = newOptions.splice(sourceIndex, 1);
    newOptions.splice(destinationIndex, 0, removedOption);

    const newQuestion: SingleChoiceQuestionRequest = { ...question, options: newOptions };
    handleUpdateQuestion(currentIndex, newQuestion);
  };

  const onDragEnd = (result: DropResult) => {
    const { destination, source } = result;
    if (!destination || destination.index === source.index) return;

    handleMoveOption(source.index, destination.index);
  };

  return (
    <div className="flex flex-col mt-8">
      <label className="mb-2 font-semibold">선택지와 정답</label>
      <DragDropContext onDragEnd={onDragEnd}>
        <Droppable droppableId="droppable">
          {(provided) => (
            <RadioGroup
              value={question.options.findIndex((option) => option.isCorrect).toString()}
              onValueChange={(value) => handleUpdateOptionCorrect(parseInt(value))}
            >
              <div ref={provided.innerRef} {...provided.droppableProps}>
                {question.options.map((option, index) => (
                  <Draggable key={index} draggableId={index.toString()} index={index}>
                    {(provided) => (
                      <div
                        ref={provided.innerRef}
                        {...provided.draggableProps}
                        {...provided.dragHandleProps}
                        className="flex items-center p-2 rounded-md mb-2 hover:bg-gray-100"
                      >
                        <Radio color="secondary" value={index.toString()} disableAnimation />
                        <input
                          type="text"
                          value={option.text}
                          onChange={(e) => handleUpdateOptionText(index, e.target.value)}
                          className="p-2 border-b border-gray-300 rounded-none ml-2 bg-transparent focus:outline-none min-w-[300px]"
                          placeholder={`${index + 1}번 옵션을 입력하세요...`}
                        />
                        {question.options.length > 2 && (
                          <button
                            onClick={() => handleRemoveOption(index)}
                            className="text-small px-3 py-2 border text-white bg-red-500 rounded-md hover:bg-red-600 ml-auto"
                          >
                            삭제
                          </button>
                        )}
                      </div>
                    )}
                  </Draggable>
                ))}
                {provided.placeholder}
              </div>
            </RadioGroup>
          )}
        </Droppable>
      </DragDropContext>
      <button
        onClick={handleAddOption}
        className="mt-4 px-4 py-2 text-black bg-gray-200 rounded-md hover:bg-gray-300 transition duration-200"
      >
        + 옵션 추가
      </button>
    </div>
  );
};

export default SingleChoiceQuestionEditor;
