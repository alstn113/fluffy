import styled from '@emotion/styled';
import { AnswerQuestionResponse } from '~/api/questionAPI';

interface LongAnswerQuestionProps {
  question: AnswerQuestionResponse;
}

const LongAnswerQuestion = ({ question }: LongAnswerQuestionProps) => {
  return (
    <Container>
      <Text>{question.text}</Text>
      <TextArea placeholder="답변을 입력하세요..." />
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

const TextArea = styled.textarea`
  flex: 1; /* 공간을 가득 채우도록 */
  padding: 1rem;
  border: 1px solid #d0d0d0;
  border-radius: 8px;
  font-size: 1rem;
  color: #333;
  resize: none; /* 크기 변경 불가 */
  transition: border-color 0.2s, box-shadow 0.2s;

  &:focus {
    border-color: #28a745; /* 포커스 시 테두리 색상 변경 */
    box-shadow: 0 0 5px rgba(40, 167, 69, 0.5); /* 포커스 시 그림자 추가 */
    outline: none; /* 기본 아웃라인 제거 */
  }

  &::placeholder {
    color: #a0a0a0; /* 플레이스홀더 색상 */
  }
`;

export default LongAnswerQuestion;
