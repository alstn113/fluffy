import styled from '@emotion/styled';
import { Suspense } from 'react';
import { useParams } from 'react-router-dom';
import BaseLayout from '~/components/layouts/BaseLayout';
import LongAnswerQuestion from '~/components/questions/details/LongAnswerQuestion';
import MultipleChoiceQuestion from '~/components/questions/details/MultipleChoiceQuestion';
import ShortAnswerQuestion from '~/components/questions/details/ShortAnswerQuestion';
import SingleChoiceQuestion from '~/components/questions/details/SingleChoiceQuestion';
import TrueOrFalseQuestion from '~/components/questions/details/TrueOrFalseQuestion';
import useGetExam from '~/hooks/api/exam/useGetExam';

const ExamDetailPage = () => {
  const { id } = useParams() as { id: string };

  return (
    <BaseLayout>
      <Suspense fallback={<div>Loading...</div>}>
        <ExamDetailPageContent examId={id} />
      </Suspense>
    </BaseLayout>
  );
};

const ExamDetailPageContent = ({ examId }: { examId: string }) => {
  const { data } = useGetExam(examId);
  const { title, description, questions } = data;
  return (
    <div>
      <h1>{title}</h1>
      <p>{description}</p>
      <Container>
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
    </div>
  );
};

const Container = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin: 0 300px;
`;

export default ExamDetailPage;
