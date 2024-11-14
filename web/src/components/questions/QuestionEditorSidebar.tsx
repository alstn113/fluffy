import { Reorder } from 'framer-motion';
import styled from '@emotion/styled';

interface QuestionEditorSidebarProps {
  problems: string[];
  setProblems: (problems: string[]) => void;
  onProblemClick: (problem: string) => void; // 클릭 이벤트 핸들러 추가
  onAddProblemType: () => void; // 문제 추가 버튼 핸들러 추가
}

const QuestionEditorSidebar = ({
  problems,
  setProblems,
  onProblemClick,
  onAddProblemType,
}: QuestionEditorSidebarProps) => {
  return (
    <Sidebar>
      <h3>문제 목록</h3>
      <Reorder.Group axis="y" values={problems} onReorder={setProblems}>
        {problems.map((problem) => (
          <ProblemItem key={problem} value={problem} onClick={() => onProblemClick(problem)}>
            {problem}
          </ProblemItem>
        ))}
      </Reorder.Group>
      <AddButton onClick={onAddProblemType}>문제 추가</AddButton>
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

const ProblemItem = styled(Reorder.Item)`
  background: #d7d7d7;
  padding: 12px;
  margin: 8px 0;
  border-radius: 4px;
  cursor: grab;
`;

const AddButton = styled.button`
  margin-top: auto;
  padding: 8px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
`;

export default QuestionEditorSidebar;
