import { DragDropContext, Draggable, Droppable, DropResult } from '@hello-pangea/dnd';
import useExamEditorStore from '@/stores/useExamEditorStore';

const ExamEditorSidebar = () => {
  const {
    questions,
    currentIndex,
    questionTypeSelectorActive,
    setQuestionTypeSelectorActive,
    handleMoveQuestion,
    handleSelectQuestion,
  } = useExamEditorStore();

  const onDragEnd = (result: DropResult) => {
    const { destination, source } = result;
    if (!destination || destination.index === source.index) return;

    handleMoveQuestion(source.index, destination.index);
  };

  return (
    <div className="flex flex-col h-full md:w-[300px] p-6 border-border-divider border-r overflow-y-scroll">
      <div className="text-lg font-semibold mb-4">문제 목록</div>
      <DragDropContext onDragEnd={onDragEnd}>
        <Droppable droppableId="droppable">
          {(provided) => (
            <div {...provided.droppableProps} ref={provided.innerRef}>
              {questions.map((item, index) => (
                <Draggable key={index} draggableId={index.toString()} index={index}>
                  {(provided) => (
                    <div
                      ref={provided.innerRef}
                      {...provided.draggableProps}
                      {...provided.dragHandleProps}
                      className={`flex items-center p-3 mb-2 rounded-md cursor-pointer ${
                        currentIndex === index && !questionTypeSelectorActive
                          ? 'bg-gray-100'
                          : 'bg-white'
                      }`}
                      onClick={() => handleSelectQuestion(index)}
                    >
                      <p
                        className={`p-2 mr-2 text-small text-white rounded-md h-6 w-6 flex items-center justify-center ${
                          currentIndex === index && !questionTypeSelectorActive
                            ? 'bg-violet-600'
                            : 'bg-gray-300'
                        }`}
                      >
                        {index + 1}
                      </p>
                      <p className="truncate">{item.text}</p>
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
        onClick={() => setQuestionTypeSelectorActive(true)}
        className="py-2 px-4 bg-gray-400 text-white rounded-md hover:bg-gray-500 transition"
      >
        문제 추가
      </button>
    </div>
  );
};

export default ExamEditorSidebar;
