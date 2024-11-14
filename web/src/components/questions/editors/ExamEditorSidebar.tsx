import { Reorder } from 'framer-motion';
import styled from '@emotion/styled';
import { QuestionBaseRequest } from '~/api/questionAPI';

interface QuestionEditorSidebarProps {
  questions: QuestionBaseRequest[];
  currentIndex: number;
  questionTypeSelectorActive: boolean;
  onQuestionTypeSelectorActive: () => void;
  onSelectQuestion: (index: number) => void;
  onReorderQuestions: (newOrder: QuestionBaseRequest[]) => void; // 추가된 prop
}

const ExamEditorSidebar = ({
  questions,
  currentIndex,
  questionTypeSelectorActive,
  onQuestionTypeSelectorActive,
  onSelectQuestion,
  onReorderQuestions, // 추가된 prop
}: QuestionEditorSidebarProps) => {
  return (
    <Sidebar>
      <h3>문제 목록</h3>
      <Reorder.Group
        axis="y"
        values={questions}
        onReorder={onReorderQuestions} // 재정렬 시 호출되는 함수
      >
        {questions.map((question, index) => (
          <QuestionItem
            key={index}
            value={question}
            isActive={index === currentIndex && !questionTypeSelectorActive}
            onClick={() => onSelectQuestion(index)}
          >
            {index + 1}. {question.text}
          </QuestionItem>
        ))}
      </Reorder.Group>
      <AddButton onClick={onQuestionTypeSelectorActive}>문제 추가</AddButton>
    </Sidebar>
  );
};

const Sidebar = styled.div`
  width: 200px; // 사이드바의 너비
  padding: 10px;
  background: #f5f5f5;
  border-right: 1px solid #ddd; // 오른쪽 경계선
  display: flex;
  flex-direction: column;
`;

const QuestionItem = styled(Reorder.Item)<{ isActive: boolean }>`
  padding: 8px;
  background: ${(props) => (props.isActive ? '#a2a2a2' : 'white')};
  cursor: pointer;
  border-radius: 4px;
  margin-bottom: 4px;
`;

const AddButton = styled.button`
  margin-top: auto;
  padding: 8px;
  background: #a2a2a2;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
`;

export default ExamEditorSidebar;
