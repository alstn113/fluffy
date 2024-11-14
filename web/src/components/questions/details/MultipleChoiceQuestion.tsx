import styled from '@emotion/styled';
import { useState } from 'react';
import { ChoiceQuestionResponse } from '~/api/questionAPI';
import { Checkbox } from '~/components/common';

interface MultipleChoiceQuestionProps {
  question: ChoiceQuestionResponse;
}

const MultipleChoiceQuestion = ({ question }: MultipleChoiceQuestionProps) => {
  const { options, sequence, text } = question;

  const [selected, setSelected] = useState<string[]>([]);

  const handleSelect = (selected: boolean, item: string) => {
    if (selected) {
      setSelected((prev) => [...prev, item]);
      return;
    }
    setSelected((prev) => prev.filter((v) => v !== item));
  };

  return (
    <Container>
      <Text>
        {sequence}. {text}
      </Text>
      <Options>
        {options.map((option) => (
          <Option key={option.id}>
            <Checkbox
              labelText={`${option.sequence}. ${option.text}`}
              color="success"
              value={option.id}
              checked={selected.includes(option.id)}
              onChange={(e) => handleSelect(e.target.checked, option.id)}
            />
          </Option>
        ))}
      </Options>
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
  font-size: 1.2rem; /* 폰트 크기 조정 */
  font-weight: 500; /* 두께 조정 */
  color: #333;
  text-align: left; /* 정렬 */
`;

const Options = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.6rem; /* 간격 조정 */
`;

const Option = styled.div`
  padding: 0.8rem; /* 패딩 조정 */
  border: 1px solid #d0d0d0;
  border-radius: 8px; /* 둥글게 */
  background-color: #f7f7f7; /* 연한 배경색 */
  transition: background-color 0.2s, border-color 0.2s;

  &:hover {
    background-color: #e9ecef; /* 호버 시 배경색 변경 */
    border-color: #a0a0a0; /* 테두리 색상 변경 */
  }
`;

export default MultipleChoiceQuestion;
