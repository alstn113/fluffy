import useExamEditorStore from '@/stores/useExamEditorStore';
import { MultipleChoiceQuestionRequest } from '@/api/questionAPI';
import { Checkbox } from '@nextui-org/checkbox';
import { DragDropContext, Draggable, Droppable, DropResult } from '@hello-pangea/dnd';

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
    const newOptions = question.options.map((option, i) =>
      i === index ? { ...option, text } : option,
    );
    const newQuestion: MultipleChoiceQuestionRequest = { ...question, options: newOptions };
    handleUpdateQuestion(currentIndex, newQuestion);
  };

  const handleUpdateOptionCorrect = (index: number, isCorrect: boolean) => {
    const newOptions = question.options.map((option, i) =>
      i === index ? { ...option, isCorrect } : option,
    );
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
    <div className="flex flex-col p-4 bg-gray-100 rounded-md shadow-md">
      <label className="mb-2 text-lg font-semibold">선택지:</label>
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
                      className="flex items-center p-2 bg-white border rounded-md mb-2 hover:bg-gray-50"
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
                        className="w-80 p-2 border border-gray-300 rounded-md ml-2"
                        style={{ minWidth: '150px' }}
                      />
                      {question.options.length > 2 && (
                        <button
                          onClick={() => handleRemoveOption(index)}
                          className="ml-2 px-3 py-1 text-white bg-red-500 rounded-md hover:bg-red-600"
                          style={{ marginLeft: 'auto' }}
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
          )}
        </Droppable>
      </DragDropContext>
      <button
        onClick={handleAddOption}
        className="mt-4 px-4 py-2 text-black bg-gray-300 rounded-md hover:bg-gray-400 transition duration-200"
      >
        + Add Option
      </button>
    </div>
  );
};

export default MultipleChoiceQuestionEditor;
