import { QuestionResponse } from '@/api/questionAPI';

export const questions: QuestionResponse[] = [
  {
    id: 1,
    text: '데이터베이스의 주요 기능 중 하나가 아닌 것은 무엇인가요?',
    type: 'SINGLE_CHOICE',
    options: [
      { id: '1', text: '데이터 저장' },
      { id: '2', text: '데이터 처리' },
      { id: '3', text: '데이터 시각화' },
    ],
  },
  {
    id: 2,
    text: '다음 중 관계형 데이터베이스에서 주 키의 역할은 무엇인가요?',
    type: 'MULTIPLE_CHOICE',
    options: [
      { id: '1', text: '각 레코드를 고유하게 식별' },
      { id: '2', text: '테이블 간의 관계를 정의' },
      { id: '3', text: '데이터를 암호화' },
      { id: '4', text: '데이터를 정규화' },
    ],
  },
  {
    id: 3,
    type: 'TRUE_OR_FALSE',
    text: 'SQL은 데이터베이스를 관리하고 쿼리하기 위한 표준 언어이다.',
    options: [
      { id: '1', text: '참' },
      { id: '2', text: '거짓' },
    ],
  },
  {
    id: 4,
    type: 'SHORT_ANSWER',
    text: '정규화의 개념과 그 중요성에 대해 설명하세요.',
  },
  {
    id: 5,
    type: 'LONG_ANSWER',
    text: 'ACID 속성에 대해 설명하고, 데이터베이스 트랜잭션에 미치는 영향을 논의하세요.',
  },
  {
    id: 6,
    text: '다음 중 SQL의 JOIN 연산의 종류가 아닌 것은?',
    type: 'MULTIPLE_CHOICE',
    options: [
      { id: '1', text: 'INNER JOIN' },
      { id: '2', text: 'LEFT JOIN' },
      { id: '3', text: 'RIGHT JOIN' },
      { id: '4', text: 'OUTER JOIN' },
      { id: '5', text: 'CROSS JOIN' },
      { id: '6', text: 'LINK JOIN' },
    ],
  },
  {
    id: 7,
    text: '데이터베이스에서 인덱스의 역할은 무엇인가요?',
    type: 'SHORT_ANSWER',
  },
  {
    id: 8,
    text: '다음 중 NoSQL 데이터베이스의 예가 아닌 것은?',
    type: 'SINGLE_CHOICE',
    options: [
      { id: '1', text: 'MongoDB' },
      { id: '2', text: 'Cassandra' },
      { id: '3', text: 'MySQL' },
      { id: '4', text: 'Redis' },
    ],
  },
];
