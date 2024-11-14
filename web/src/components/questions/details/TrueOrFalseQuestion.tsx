import styled from '@emotion/styled';
import { useState } from 'react';
import { ChoiceQuestionResponse } from '~/api/questionAPI.ts';

interface TrueOrFalseQuestionProps {
  question: ChoiceQuestionResponse;
}

const TrueOrFalseQuestion = ({ question }: TrueOrFalseQuestionProps) => {
  const { sequence, text } = question;
  const [selected, setSelected] = useState<string | null>(null);

  const handleSelect = (value: string) => {
    setSelected(value);
  };

  return (
    <Container>
      <Text>
        {sequence}. {text}
      </Text>
      <ButtonContainer>
        <Button
          className={`true ${selected === 'true' ? 'selected' : ''}`}
          onClick={() => handleSelect('true')}
        >
          참
        </Button>
        <Button
          className={`false ${selected === 'false' ? 'selected' : ''}`}
          onClick={() => handleSelect('false')}
        >
          거짓
        </Button>
      </ButtonContainer>
    </Container>
  );
};

const Container = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 1.5rem;
  border-radius: 12px; /* 더 둥글게 */
  background-color: #ffffff; /* 배경색을 흰색으로 */
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s, transform 0.2s;
`;

const Text = styled.div`
  font-size: 1.25rem;
  font-weight: 500;
  color: #333;
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: space-between; /* 버튼을 양쪽으로 분배 */
`;

const Button = styled.button`
  flex: 1; /* 버튼을 동일한 크기로 만듭니다 */
  padding: 1rem;
  margin: 0 0.5rem; /* 버튼 사이의 간격 */
  border: none;
  border-radius: 8px;
  color: #333;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s, transform 0.2s, box-shadow 0.2s;

  &.true {
    background-color: #d4edda; /* '참' 버튼 기본 색상 */
  }

  &.false {
    background-color: #f8d7da; /* '거짓' 버튼 기본 색상 */
  }

  &:hover {
    background-color: #c3e6cb; /* '참' 버튼 호버 색상 */
  }

  &:hover.false {
    background-color: #f5c6cb; /* '거짓' 버튼 호버 색상 */
  }

  &.selected {
    color: white; /* 텍스트 색상 변경 */
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2); /* 그림자 추가 */
    transform: scale(1.05); /* 약간 커지는 효과 */
  }

  &.selected.true {
    background-color: #28a745; /* 선택된 '참' 버튼 색상 */
  }

  &.selected.false {
    background-color: #dc3545; /* 선택된 '거짓' 버튼 색상 */
  }

  &:active {
    transform: translateY(1px); /* 클릭 시 약간 눌리는 효과 */
  }
`;

export default TrueOrFalseQuestion;
