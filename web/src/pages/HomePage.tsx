import styled from '@emotion/styled';
import { Toggle } from '~/components/common';
import BaseLayout from '~/components/layouts/BaseLayout';
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
      </Container>
    </BaseLayout>
  );
};

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  gap: 3rem;
  margin: 2rem auto;
`;

export default HomePage;
