import styled from '@emotion/styled';
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
    <Sidebar>
      <h3>문제 목록</h3>
      <DragDropContext onDragEnd={onDragEnd}>
        <Droppable droppableId="droppable">
          {(provided) => (
            <FormListContainer {...provided.droppableProps} ref={provided.innerRef}>
              {questions.map((item, index) => (
                <Draggable key={index} draggableId={index.toString()} index={index}>
                  {(provided) => (
                    <Container
                      ref={provided.innerRef}
                      {...provided.draggableProps}
                      {...provided.dragHandleProps}
                      isActive={currentIndex === index && !questionTypeSelectorActive}
                      onClick={() => handleSelectQuestion(index)}
                    >
                      {index + 1}. {item.text}
                    </Container>
                  )}
                </Draggable>
              ))}
              {provided.placeholder}
            </FormListContainer>
          )}
        </Droppable>
      </DragDropContext>
      <AddButton onClick={() => setQuestionTypeSelectorActive(true)}>문제 추가</AddButton>
    </Sidebar>
  );
};

const Sidebar = styled.div`
  width: 250px;
  padding: 16px;
  background: #f9f9f9;
  border-right: 1px solid #ddd;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
`;

const AddButton = styled.button`
  margin-top: auto;
  padding: 10px 16px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.2s;

  &:hover {
    background: #0056b3;
  }
`;

const FormListContainer = styled.div`
  padding: 8px;
  background-color: #f9f9f9;
  flex-grow: 1;
  min-height: 30px;
  border-radius: 4px;
`;

const Container = styled.div<{ isActive: boolean }>`
  border-radius: 4px;
  padding: 12px;
  min-height: 50px;
  display: flex;
  align-items: center;
  cursor: pointer;
  justify-content: flex-start;
  margin-bottom: 8px;
  background-color: ${(props) => (props.isActive ? '#d1ecf1' : 'white')};
`;

export default ExamEditorSidebar;
