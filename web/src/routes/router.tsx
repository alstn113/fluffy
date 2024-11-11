import { createBrowserRouter, RouteObject, RouterProvider } from 'react-router-dom';
import { PAGE_LIST } from '~/constants';
import AboutPage from '~/pages/AboutPage';
import ExamEditPage from '~/pages/exams/ExamEditPage.tsx';
import ExamListPage from '~/pages/exams/ExamListPage.tsx';
import HomePage from '~/pages/HomePage';
import NotFoundPage from '~/pages/NotFoundPage';

const routes: RouteObject[] = [
  {
    path: PAGE_LIST.home,
    element: <HomePage />,
  },
  {
    path: PAGE_LIST.about,
    element: <AboutPage />,
  },
  {
    path: PAGE_LIST.exam.list,
    element: <ExamListPage />,
  },
  {
    path: PAGE_LIST.exam.edit,
    element: <ExamEditPage />,
  },
  {
    path: PAGE_LIST.notFound,
    element: <NotFoundPage />,
  },
];

const router = createBrowserRouter(routes);

export const MyRouterProvider = () => {
  return <RouterProvider router={router} />;
};
