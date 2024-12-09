import useExamEditorStore from '@/stores/useExamEditorStore';
import { MultipleChoiceQuestionRequest } from '@/api/questionAPI';
import { DragDropContext, Draggable, Droppable, DropResult } from '@hello-pangea/dnd';
import { Button, Checkbox, Tooltip } from '@nextui-org/react';
import { FaTrashAlt } from 'react-icons/fa';
import { BsFillQuestionCircleFill } from 'react-icons/bs';

const MultipleChoiceQuestionEditor = () => {
  const { handleUpdateQuestion, currentIndex, questions } = useExamEditorStore();
  const question = questions[currentIndex] as MultipleChoiceQuestionRequest;

  const handleAddOption = () => {
    const newOption = { text: `옵션 ${question.options.length + 1}`, isCorrect: false };
    const newQuestion: MultipleChoiceQuestionRequest = {
      ...question,
      options: [...question.options, newOption],
    };
    handleUpdateQuestion(currentIndex, newQuestion);
  };

  const handleRemoveOption = (index: number) => {
    const newOptions = question.options.filter((_, i) => i !== index);
    const newQuestion: MultipleChoiceQuestionRequest = { ...question, options: newOptions };
    handleUpdateQuestion(currentIndex, newQuestion);
  };

  const handleUpdateOptionText = (index: number, text: string) => {
    const newOptions = question.options.map((option, i) => (i === index ? { ...option, text } : option));
    const newQuestion: MultipleChoiceQuestionRequest = { ...question, options: newOptions };
    handleUpdateQuestion(currentIndex, newQuestion);
  };

  const handleUpdateOptionCorrect = (index: number, isCorrect: boolean) => {
    const newOptions = question.options.map((option, i) => (i === index ? { ...option, isCorrect } : option));
    const newQuestion: MultipleChoiceQuestionRequest = { ...question, options: newOptions };
    handleUpdateQuestion(currentIndex, newQuestion);
  };

  const handleMoveOption = (sourceIndex: number, destinationIndex: number) => {
    const newOptions = [...question.options];
    const [removedOption] = newOptions.splice(sourceIndex, 1);
    newOptions.splice(destinationIndex, 0, removedOption);

    const newQuestion: MultipleChoiceQuestionRequest = { ...question, options: newOptions };
    handleUpdateQuestion(currentIndex, newQuestion);
  };

  const onDragEnd = (result: DropResult) => {
    const { destination, source } = result;
    if (!destination || destination.index === source.index) return;

    handleMoveOption(source.index, destination.index);
  };

  return (
    <div className="flex flex-col mt-8">
      <div className="flex items-center gap-2 mb-2 ">
        <label className="font-semibold">선택지와 정답</label>
        <div>
          <Tooltip
            placement="right"
            color="foreground"
            showArrow
            content={
              <div className="text-sm">
                <p>선택지를 드래그하여</p>
                <p>순서를 변경할 수 있습니다.</p>
              </div>
            }
          >
            <div>
              <BsFillQuestionCircleFill size={16} className="text-gray-500" />
            </div>
          </Tooltip>
        </div>
      </div>
      <DragDropContext onDragEnd={onDragEnd}>
        <Droppable droppableId="droppable">
          {(provided) => (
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
                      <Checkbox
                        color="secondary"
                        isSelected={option.isCorrect}
                        onValueChange={() => handleUpdateOptionCorrect(index, !option.isCorrect)}
                        disableAnimation
                      />
                      <input
                        type="text"
                        value={option.text}
                        onChange={(e) => handleUpdateOptionText(index, e.target.value)}
                        className="p-2 border-b border-gray-300 rounded-none ml-2 bg-transparent focus:outline-none min-w-[300px]"
                        placeholder={`${index + 1}번 옵션을 입력하세요...`}
                      />
                      {question.options.length > 2 && (
                        <Button
                          color="danger"
                          variant="shadow"
                          size="sm"
                          isIconOnly
                          onPress={() => handleRemoveOption(index)}
                          className="ml-auto"
                        >
                          <FaTrashAlt size={12} />
                        </Button>
                      )}
                    </div>
                  )}
                </Draggable>
              ))}
              {provided.placeholder}
            </div>
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

export default MultipleChoiceQuestionEditor;
