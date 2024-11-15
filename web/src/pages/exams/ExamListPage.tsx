import { Link } from 'react-router-dom';
import BaseLayout from '~/components/layouts/BaseLayout';
import useGetExams from '~/hooks/api/exam/useGetExams';

const ExamListPage = () => {
  return (
    <BaseLayout>
      <ExamListPageContent />
    </BaseLayout>
  );
};

const ExamListPageContent = () => {
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
