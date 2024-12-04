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
  Tab,
  Tabs,
} from '@nextui-org/react';
import { GITHUB_OAUTH_LOGIN_URL, Routes } from '@/constants';
import useLogout from '@/hooks/useLogout.ts';
import { useLocation, useParams } from 'react-router';
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
              <DropdownItem key="dashboard" href={Routes.dashboard()}>
                Dashboard
              </DropdownItem>
              <DropdownItem
                key="logout"
                className="text-danger"
                color="danger"
                onPress={() => logout()}
              >
                Logout
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
  const { pathname } = useLocation();

  return (
    <Tabs selectedKey={pathname} variant={'underlined'} color={'success'} size={'lg'}>
      <Tab
        as={Link}
        key={Routes.exam.management.overview(examId)}
        id={Routes.exam.management.overview(examId)}
        href={Routes.exam.management.overview(examId)}
        title={'Overview'}
      />
      <Tab
        as={Link}
        key={Routes.exam.management.questions(examId)}
        id={Routes.exam.management.questions(examId)}
        href={Routes.exam.management.questions(examId)}
        title={'Questions'}
      />
      <Tab
        as={Link}
        key={Routes.exam.management.analytics(examId)}
        id={Routes.exam.management.analytics(examId)}
        href={Routes.exam.management.analytics(examId)}
        title={'Analytics'}
        isDisabled={data.status !== EXAM_STATUS.published}
      />
      <Tab
        as={Link}
        key={Routes.exam.management.settings(examId)}
        id={Routes.exam.management.settings(examId)}
        href={Routes.exam.management.settings(examId)}
        title={'Settings'}
      />
    </Tabs>
  );
};

export default ExamManagementHeader;
