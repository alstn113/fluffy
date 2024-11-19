import useExamEditorStore from '@/stores/useExamEditorStore';
import { MultipleChoiceQuestionRequest } from '@/api/questionAPI';
import styled from '@emotion/styled';
import { Checkbox, CheckboxGroup } from '@nextui-org/checkbox';
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
    <Container>
      <Label>선택지:</Label>
      <DragDropContext onDragEnd={onDragEnd}>
        <Droppable droppableId="droppable">
          {(provided) => (
            <div ref={provided.innerRef} {...provided.droppableProps}>
              {question.options.map((option, index) => (
                <Draggable key={index} draggableId={index.toString()} index={index}>
                  {(provided) => (
                    <Option
                      ref={provided.innerRef}
                      {...provided.draggableProps}
                      {...provided.dragHandleProps}
                    >
                      <Checkbox
                        color="secondary"
                        isSelected={option.isCorrect}
                        onValueChange={() => handleUpdateOptionCorrect(index, !option.isCorrect)}
                        disableAnimation
                      />
                      <Input
                        type="text"
                        value={option.text}
                        onChange={(e) => handleUpdateOptionText(index, e.target.value)}
                      />
                      {question.options.length > 2 && (
                        <RemoveButton onClick={() => handleRemoveOption(index)}>삭제</RemoveButton>
                      )}
                    </Option>
                  )}
                </Draggable>
              ))}
              {provided.placeholder}
            </div>
          )}
        </Droppable>
      </DragDropContext>
      <AddButton onClick={handleAddOption}>+ Add Option</AddButton>
    </Container>
  );
};

// 스타일 컴포넌트
const Container = styled.div`
  display: flex;
  flex-direction: column;
  padding: 16px;
  background: #f9f9f9;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
`;

const Label = styled.label`
  margin: 8px 0 4px;
`;

const Input = styled.input`
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
`;

const Option = styled.div`
  display: flex;
  align-items: center;

  padding: 0.8rem; /* 패딩 조정 */
  background-color: #f7f7f7; /* 연한 배경색 */
  transition: background-color 0.2s, border-color 0.2s;

  &:hover {
    background-color: #e9ecef; /* 호버 시 배경색 변경 */
    border-color: #a0a0a0; /* 테두리 색상 변경 */
  }
`;

const RemoveButton = styled.button`
  margin-left: 8px;
  padding: 6px 12px;
  background: #ff4d4d;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;

  &:hover {
    background: #ff1a1a;
  }
`;

const AddButton = styled.button`
  margin-top: 12px;
  padding: 8px 12px;
  color: #000000;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.2s;

  &:hover {
    background: #ececec;
  }
`;

export default MultipleChoiceQuestionEditor;
