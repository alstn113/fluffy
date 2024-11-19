import styled from '@emotion/styled';
import { useState } from 'react';
import { ChoiceQuestionResponse } from '@/api/questionAPI.ts';
import { Radio, RadioGroup } from '@nextui-org/radio';

interface SingleChoiceQuestionProps {
  question: ChoiceQuestionResponse;
}

const SingleChoiceQuestion = ({ question }: SingleChoiceQuestionProps) => {
  const { options, text } = question;
  const [selected, setSelected] = useState<string | null>(null);

  return (
    <Container>
      <Text>{text}</Text>
      <RadioGroup value={selected} onValueChange={setSelected}>
        {options.map((option, index) => (
          <Radio color="secondary" key={option.id} value={option.id}>
            {index + 1}. {option.text}
          </Radio>
        ))}
      </RadioGroup>
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

export default SingleChoiceQuestion;
