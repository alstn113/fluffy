import { createBrowserRouter, RouteObject, RouterProvider } from 'react-router-dom';
import { PAGE_LIST } from '~/constants';
import AboutPage from '~/pages/AboutPage';
import FormEditPage from '~/pages/forms/FormEditPage';
import FormListPage from '~/pages/forms/FormListPage';
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
    path: PAGE_LIST.form.list,
    element: <FormListPage />,
  },
  {
    path: PAGE_LIST.form.edit,
    element: <FormEditPage />,
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
