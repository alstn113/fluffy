import { createBrowserRouter, RouteObject, RouterProvider } from 'react-router-dom';
import { PAGE_LIST } from '~/constants';
import AboutPage from '~/pages/AboutPage';
import ExamEditorPage from '~/pages/exams/ExamEditPage';
import ExamListPage from '~/pages/exams/ExamListPage';
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
    element: <ExamEditorPage />,
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
