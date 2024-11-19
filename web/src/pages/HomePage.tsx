import { QuestionResponse } from '@/api/questionAPI';
import BaseLayout from '@/components/layouts/BaseLayout';
import NewExamButton from '@/components/exams/NewExamButton';
import { Link } from 'react-router-dom';
import QuestionDetailTemplate from '@/components/questions/details/QuestionDetailTemplate';

const HomePage = () => {
  return (
    <BaseLayout>
      <div className="flex flex-col justify-center items-start gap-4 mx-auto my-8">
        <h1 className="text-2xl font-bold">
          Home Page
          <NewExamButton />
        </h1>
        <div>
          <Link to="/exams" className="text-blue-500 hover:underline">
            Go To Exam List Page
          </Link>
        </div>

        <div className="flex flex-col gap-4">
          {questions.map((question, index) => {
            return <QuestionDetailTemplate key={question.id} question={question} index={index} />;
          })}
        </div>
      </div>
    </BaseLayout>
  );
};

const questions: QuestionResponse[] = [
  {
    id: 1,
    text: '운영체제의 주요 기능 중 하나가 아닌 것은 무엇인가요?',
    type: 'SINGLE_CHOICE',
    options: [
      { id: '1', text: '프로세스 관리' },
      { id: '2', text: '메모리 관리' },
      { id: '3', text: '데이터베이스 관리' },
    ],
  },
  {
    id: 2,
    text: '다음 중 운영체제의 스케줄링 알고리즘이 아닌 것은?',
    type: 'MULTIPLE_CHOICE',
    options: [
      { id: '1', text: '라운드 로빈' },
      { id: '2', text: '우선순위 스케줄링' },
      { id: '3', text: 'FIFO' },
      { id: '4', text: '링크드 리스트' },
    ],
  },
  {
    id: 3,
    type: 'TRUE_OR_FALSE',
    text: '운영체제는 하드웨어와 소프트웨어 간의 인터페이스 역할을 한다.',
    options: [
      { id: '1', text: '참' },
      { id: '2', text: '거짓' },
    ],
  },
  {
    id: 4,
    type: 'SHORT_ANSWER',
    text: '운영체제의 핵심 기능 중 하나인 ____는 프로세스 간의 통신과 동기화를 담당합니다.',
  },
  {
    id: 5,
    type: 'LONG_ANSWER',
    text: '가상 메모리의 개념과 장점에 대해 설명해 주세요.',
  },
];

export default HomePage;
