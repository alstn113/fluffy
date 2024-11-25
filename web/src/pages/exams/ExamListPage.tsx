import { Link } from 'react-router-dom';
import BaseLayout from '@/components/layouts/base/BaseLayout.tsx';
import useGetExams from '@/hooks/api/exam/useGetExams';
import AsyncBoundary from '@/components/AsyncBoundary';

const ExamListPage = () => {
  return (
    <BaseLayout>
      <AsyncBoundary>
        <ExamListContent />
      </AsyncBoundary>
    </BaseLayout>
  );
};

const ExamListContent = () => {
  const { data } = useGetExams();

  return (
    <div>
      <h1>Exams</h1>
      <ul>
        {data?.map((exam) => (
          <Link to={`/exams/${exam.id}`} key={exam.id}>
            <li>
              {exam.id} {exam.title}
            </li>
          </Link>
        ))}
      </ul>
    </div>
  );
};

export default ExamListPage;
