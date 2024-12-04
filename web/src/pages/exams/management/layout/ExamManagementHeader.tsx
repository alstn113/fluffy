import {
  Button,
  Dropdown,
  DropdownItem,
  DropdownMenu,
  DropdownTrigger,
  Link,
  Navbar,
  NavbarBrand,
  NavbarContent,
  NavbarItem,
} from '@nextui-org/react';
import { GITHUB_OAUTH_LOGIN_URL, Routes } from '@/constants';
import useLogout from '@/hooks/useLogout.ts';
import { NavLink, useLocation, useParams } from 'react-router';
import useUser from '@/hooks/useUser.ts';
import { Avatar } from '@daveyplate/nextui-fixed-avatar';
import AsyncBoundary from '@/components/AsyncBoundary.tsx';
import useGetExamWithAnswers from '@/hooks/api/exam/useGetExamWithAnswers.ts';
import { EXAM_STATUS } from '@/api/examAPI.ts';

const ExamManagementHeader = () => {
  const params = useParams() as { examId: string };
  const examId = Number(params.examId);

  const user = useUser();
  const logout = useLogout();
  const location = useLocation();

  const handleGithubLogin = () => {
    window.location.href = `${GITHUB_OAUTH_LOGIN_URL}?next=${location.pathname}`;
  };

  return (
    <Navbar>
      <NavbarBrand>
        <Link href={Routes.home()} color={'foreground'}>
          <p className="font-bold text-inherit">Pass</p>
        </Link>
      </NavbarBrand>

      <NavbarContent justify="center">
        {/*TODO: suspense, errorboundary 범위 정하고, Fallback 디테일하게 변경*/}
        <AsyncBoundary>
          <ExamManagementHeaderCenterContent examId={examId} />
        </AsyncBoundary>
      </NavbarContent>

      <NavbarContent as="div" justify="end">
        {user ? (
          <Dropdown placement="bottom-end">
            <DropdownTrigger>
              <Avatar isBordered as="button" color="primary" size="sm" src={user.avatarUrl} />
            </DropdownTrigger>
            <DropdownMenu aria-label="Profile Actions" variant="flat">
              <DropdownItem key="home" href={Routes.home()}>
                Home
              </DropdownItem>
              <DropdownItem
                key="logout"
                className="text-danger"
                color="danger"
                onPress={() => logout()}
              >
                Log Out
              </DropdownItem>
            </DropdownMenu>
          </Dropdown>
        ) : (
          <NavbarItem>
            <Button onPress={handleGithubLogin}>Login</Button>
          </NavbarItem>
        )}
      </NavbarContent>
    </Navbar>
  );
};

const ExamManagementHeaderCenterContent = ({ examId }: { examId: number }) => {
  const { data } = useGetExamWithAnswers(examId);

  return (
    <>
      <NavbarItem>
        <NavLink
          to={Routes.exam.management.overview(examId)}
          className={({ isActive }) => (isActive ? 'text-blue-500' : 'text-black')}
        >
          Overview
        </NavLink>
      </NavbarItem>
      <NavbarItem>
        <NavLink
          to={Routes.exam.management.questions(examId)}
          className={({ isActive }) => (isActive ? 'text-blue-500' : 'text-black')}
        >
          Questions
        </NavLink>
      </NavbarItem>
      <NavbarItem>
        <NavLink
          to={Routes.exam.management.analytics(examId)}
          className={({ isActive }) =>
            `${isActive ? 'text-blue-500' : 'text-black'} ${
              data.status !== EXAM_STATUS.published
                ? 'opacity-50 cursor-default pointer-events-none'
                : ''
            }`
          }
        >
          Analytics
        </NavLink>
      </NavbarItem>
      <NavbarItem>
        <NavLink
          to={Routes.exam.management.settings(examId)}
          className={({ isActive }) => (isActive ? 'text-blue-500' : 'text-black')}
        >
          Settings
        </NavLink>
      </NavbarItem>
    </>
  );
};

export default ExamManagementHeader;
