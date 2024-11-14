import styled from '@emotion/styled';

const ProblemEditor = ({ problem, onClose }: any) => {
  return (
    <EditorContainer>
      <h3>{problem} 편집하기</h3>
      {/* 문제 편집 로직을 추가 */}
      <CloseButton onClick={onClose}>닫기</CloseButton>
    </EditorContainer>
  );
};

const EditorContainer = styled.div`
  padding: 10px;
  background: #f9f9f9;
  border: 1px solid #ccc;
  border-radius: 4px;
`;

const CloseButton = styled.button`
  margin-top: 10px;
  padding: 8px;
  background: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
`;

export default ProblemEditor;
