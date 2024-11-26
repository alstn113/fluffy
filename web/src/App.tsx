import { Route, Routes } from 'react-router-dom';
import { PAGE_LIST } from '@/constants';
import AboutPage from '@/pages/AboutPage';
import ExamDetailPage from '@/pages/exams/ExamDetailPage';
import ExamEditorPage from '@/pages/exams/ExamEditPage';
import ExamListPage from '@/pages/exams/ExamListPage';
import HomePage from '@/pages/HomePage';
import DashBoardPage from '@/pages/dashboard/DashboardPage';
import NotFoundPage from '@/pages/NotFoundPage';
import useGetMe from './hooks/useGetMe';

const App = () => {
  useGetMe();

  return (
    <Routes>
      <Route element={<HomePage />} path={PAGE_LIST.home} />
      <Route element={<AboutPage />} path={PAGE_LIST.about} />
      <Route element={<ExamListPage />} path={PAGE_LIST.exam.list} />
      <Route element={<ExamDetailPage />} path={PAGE_LIST.exam.detail} />
      <Route element={<ExamEditorPage />} path={PAGE_LIST.exam.edit} />
      <Route element={<DashBoardPage />} path={PAGE_LIST.dashboard.list} />
      <Route element={<NotFoundPage />} path={PAGE_LIST.notFound} />
    </Routes>
  );
};

export default App;
