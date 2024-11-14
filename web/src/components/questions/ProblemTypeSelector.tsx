import styled from '@emotion/styled';

const problemTypes = [
  { label: '단답형', value: 'SHORT_ANSWER' },
  { label: '서술형', value: 'LONG_ANSWER' },
  { label: '단일 선택 객관식', value: 'SINGLE_CHOICE' },
  { label: '복수 선택 객관식', value: 'MULTIPLE_CHOICE' },
  { label: 'OX 문제', value: 'TRUE_OR_FALSE' },
];

const ProblemTypeSelector = ({ onClose }: any) => {
  const handleSelectType = (type: any) => {
    // 문제 유형 선택 후 처리 로직 (문제 추가 등)
    console.log(`선택된 문제 유형: ${type}`);
    onClose(); // 선택 후 닫기
  };

  return (
    <SelectorContainer>
      <h3>문제 유형 선택</h3>
      {problemTypes.map((type) => (
        <TypeItem key={type.value} onClick={() => handleSelectType(type.value)}>
          {type.label}
        </TypeItem>
      ))}
      <CloseButton onClick={onClose}>닫기</CloseButton>
    </SelectorContainer>
  );
};

const SelectorContainer = styled.div`
  padding: 10px;
  background: #f9f9f9;
  border: 1px solid #ccc;
  border-radius: 4px;
`;

const TypeItem = styled.div`
  padding: 8px;
  cursor: pointer;
  &:hover {
    background: #f0f0f0;
  }
`;

const CloseButton = styled.button`
  margin-top: 10px;
  padding: 8px;
  background: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
`;

export default ProblemTypeSelector;
