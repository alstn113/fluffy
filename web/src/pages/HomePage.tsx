import styled from '@emotion/styled';
import { QuestionResponse } from '~/api/questionAPI';
import { Toggle } from '~/components/common';
import BaseLayout from '~/components/layouts/BaseLayout';
import LongAnswerQuestion from '~/components/questions/details/LongAnswerQuestion';
import ShortAnswerQuestion from '~/components/questions/details/ShortAnswerQuestion';
import MultipleChoiceQuestion from '~/components/questions/details/MultipleChoiceQuestion';
import SingleChoiceQuestion from '~/components/questions/details/SingleChoiceQuestion';
import TrueOrFalseQuestion from '~/components/questions/details/TrueOrFalseQuestion';
import NewExamButton from '~/components/exams/NewExamButton';
import { Link } from 'react-router-dom';

const HomePage = () => {
  return (
    <BaseLayout>
      <Container>
        <h1>
          Home Page <Toggle labelText="Toggle" color="success" />
          <NewExamButton />
        </h1>
        <div>
          <Link to="/exams">Go To Exam List Page</Link>
        </div>

        {questions.map((question) => {
          switch (question.type) {
            case 'SINGLE_CHOICE':
              return <SingleChoiceQuestion key={question.id} question={question} />;
            case 'MULTIPLE_CHOICE':
              return <MultipleChoiceQuestion key={question.id} question={question} />;
            case 'TRUE_OR_FALSE':
              return <TrueOrFalseQuestion key={question.id} question={question} />;
            case 'SHORT_ANSWER':
              return <ShortAnswerQuestion key={question.id} question={question} />;
            case 'LONG_ANSWER':
              return <LongAnswerQuestion key={question.id} question={question} />;
          }
        })}
      </Container>
    </BaseLayout>
  );
};

const questions: QuestionResponse[] = [
  {
    id: 1,
    text: '운영체제의 주요 기능 중 하나가 아닌 것은 무엇인가요?',
    sequence: 1,
    type: 'SINGLE_CHOICE',
    options: [
      {
        id: '1',
        text: '프로세스 관리',
        sequence: 1,
      },
      {
        id: '2',
        text: '메모리 관리',
        sequence: 2,
      },
      {
        id: '3',
        text: '데이터베이스 관리',
        sequence: 3,
      },
    ],
  },
  {
    id: 2,
    text: '다음 중 운영체제의 스케줄링 알고리즘이 아닌 것은?',
    sequence: 2,
    type: 'MULTIPLE_CHOICE',
    options: [
      {
        id: '1',
        text: '라운드 로빈',
        sequence: 1,
      },
      {
        id: '2',
        text: '우선순위 스케줄링',
        sequence: 2,
      },
      {
        id: '3',
        text: 'FIFO',
        sequence: 3,
      },
      {
        id: '4',
        text: '링크드 리스트',
        sequence: 4,
      },
    ],
  },
  {
    id: 3,
    type: 'TRUE_OR_FALSE',
    sequence: 3,
    text: '운영체제는 하드웨어와 소프트웨어 간의 인터페이스 역할을 한다.',
    options: [
      {
        id: '1',
        text: '참',
        sequence: 1,
      },
      {
        id: '2',
        text: '거짓',
        sequence: 2,
      },
    ],
  },
  {
    id: 4,
    type: 'SHORT_ANSWER',
    sequence: 4,
    text: '운영체제의 핵심 기능 중 하나인 ____는 프로세스 간의 통신과 동기화를 담당합니다.',
  },
  {
    id: 5,
    type: 'LONG_ANSWER',
    sequence: 5,
    text: '가상 메모리의 개념과 장점에 대해 설명해 주세요.',
  },
];

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  gap: 3rem;
  margin: 2rem auto;
`;

export default HomePage;
