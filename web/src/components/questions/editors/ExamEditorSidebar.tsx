import styled from '@emotion/styled';
import { QuestionBaseRequest } from '~/api/questionAPI';
import { DragDropContext, Draggable, Droppable, DropResult } from '@hello-pangea/dnd';

interface QuestionEditorSidebarProps {
  questions: QuestionBaseRequest[];
  currentIndex: number;
  questionTypeSelectorActive: boolean;
  onQuestionTypeSelectorActive: () => void;
  onSelectQuestion: (index: number) => void;
  onReorderQuestions: (newOrder: QuestionBaseRequest[]) => void;
}

const ExamEditorSidebar = ({
  questions,
  currentIndex,
  questionTypeSelectorActive,
  onQuestionTypeSelectorActive,
  onSelectQuestion,
  onReorderQuestions,
}: QuestionEditorSidebarProps) => {
  const reorder = (list: any[], startIndex: number, endIndex: number) => {
    const result = Array.from(list);
    const [removed] = result.splice(startIndex, 1);
    result.splice(endIndex, 0, removed);
    return result;
  };

  const onDragEnd = (result: DropResult) => {
    const { destination, source } = result;
    if (!destination || destination.index === source.index) return;

    const newQuestions = reorder(questions, source.index, destination.index);
    onReorderQuestions(newQuestions);
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
                      onClick={() => onSelectQuestion(index)}
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
      <AddButton onClick={onQuestionTypeSelectorActive}>문제 추가</AddButton>
    </Sidebar>
  );
};

const Sidebar = styled.div`
  width: 250px; // 사이드바의 너비
  padding: 16px; // 패딩 증가
  background: #f9f9f9; // 부드러운 배경색
  border-right: 1px solid #ddd;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1); // 약간의 그림자 추가
`;

const AddButton = styled.button`
  margin-top: auto;
  padding: 10px 16px; // 패딩 증가
  background: #007bff; // 기본 색상 변경
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.2s;

  &:hover {
    background: #0056b3; // 호버 효과
  }
`;

const FormListContainer = styled.div`
  padding: 8px;
  background-color: #f9f9f9; // 배경색 변경
  flex-grow: 1;
  min-height: 30px;
  border-radius: 4px;
`;

const Container = styled.div<{ isActive: boolean }>`
  border-radius: 4px;
  padding: 12px;
  min-height: 50px; // 최소 높이 증가
  display: flex;
  align-items: center;
  cursor: pointer;
  justify-content: flex-start;
  margin-bottom: 8px;
  background-color: ${(props) => (props.isActive ? '#d1ecf1' : 'white')};
  transition: background-color 0.2s, border-color 0.2s; // 부드러운 전환 효과
`;

export default ExamEditorSidebar;
