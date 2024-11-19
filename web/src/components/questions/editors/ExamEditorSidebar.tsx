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
    <div className="w-64 h-full p-4 bg-gray-100 border-r border-gray-300 flex flex-col shadow-lg">
      <h3 className="text-lg font-semibold mb-4">문제 목록</h3>
      <DragDropContext onDragEnd={onDragEnd}>
        <Droppable droppableId="droppable">
          {(provided) => (
            <div
              {...provided.droppableProps}
              ref={provided.innerRef}
              className="flex-grow bg-gray-100 rounded-lg p-2 overflow-y-auto" // 스크롤 가능하게 추가
            >
              {questions.map((item, index) => (
                <Draggable key={index} draggableId={index.toString()} index={index}>
                  {(provided) => (
                    <div
                      ref={provided.innerRef}
                      {...provided.draggableProps}
                      {...provided.dragHandleProps}
                      className={`flex items-center p-3 mb-2 rounded-md cursor-pointer ${
                        currentIndex === index && !questionTypeSelectorActive
                          ? 'bg-blue-100'
                          : 'bg-white'
                      }`}
                      onClick={() => handleSelectQuestion(index)}
                    >
                      {index + 1}. {item.text}
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
        className="mt-auto py-2 px-4 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition"
      >
        문제 추가
      </button>
    </div>
  );
};

export default ExamEditorSidebar;
