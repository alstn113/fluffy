// vercel
import { SpeedInsights } from '@vercel/speed-insights/react';
import { Analytics } from '@vercel/analytics/react';

import { Route, Routes, useHref, useNavigate } from 'react-router';
import AboutPage from '@/pages/AboutPage';
import ExamDetailPage from '@/pages/exams/ExamDetailPage';
import ExamManagementQuestionsPage from '@/pages/exams/management/ExamManagementQuestionsPage.tsx';
import HomePage from '@/pages/HomePage';
import DashBoardPage from '@/pages/dashboard/DashboardPage';
import NotFoundPage from '@/pages/NotFoundPage';
import useGetMe from '@/hooks/useGetMe';
import ExamManagementOverviewPage from '@/pages/exams/management/ExamManagementOverviewPage';
import ExamManagementAnalyticsPage from '@/pages/exams/management/ExamManagementAnalyticsPage';
import ExamManagementSettingsPage from '@/pages/exams/management/ExamManagementSettingsPage';
import ExamManagementLayout from '@/pages/exams/management/layout/ExamManagementLayout.tsx';
import BaseLayout from '@/components/layouts/base/BaseLayout.tsx';
import { NextUIProvider } from '@nextui-org/system';

const App = () => {
  const navigate = useNavigate();

  useGetMe();

  return (
    <NextUIProvider navigate={navigate} useHref={useHref}>
      <Routes>
        <Route element={<BaseLayout />}>
          <Route index element={<HomePage />} />
          <Route path="about" element={<AboutPage />} />
          <Route path="dashboard" element={<DashBoardPage />} />
          <Route path="exams/:examId" element={<ExamDetailPage />} />
          <Route path="*" element={<NotFoundPage />} />
        </Route>
        <Route path="exams/:examId/management" element={<ExamManagementLayout />}>
          <Route path="overview" element={<ExamManagementOverviewPage />} />
          <Route path="questions" element={<ExamManagementQuestionsPage />} />
          <Route path="analytics" element={<ExamManagementAnalyticsPage />} />
          <Route path="settings" element={<ExamManagementSettingsPage />} />
        </Route>
      </Routes>
      <SpeedInsights />
      <Analytics />
    </NextUIProvider>
  );
};

export default App;
