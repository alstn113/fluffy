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
import { GITHUB_OAUTH_LOGIN_URL, PAGE_LIST } from '@/constants';
import useLogout from '@/hooks/useLogout.ts';
import { useLocation } from 'react-router-dom';
import useUser from '@/hooks/useUser.ts';
import { Avatar } from '@daveyplate/nextui-fixed-avatar';

const EditorHeader = () => {
  const user = useUser();
  const logout = useLogout();
  const location = useLocation();

  const handleGithubLogin = () => {
    window.location.href = `${GITHUB_OAUTH_LOGIN_URL}?next=${location.pathname}`;
  };

  return (
    <Navbar>
      <NavbarBrand>
        <Link href={PAGE_LIST.home} color={'foreground'}>
          <p className="font-bold text-inherit">Pass</p>
        </Link>
      </NavbarBrand>

      <NavbarContent as="div" justify="end">
        {user ? (
          <Dropdown placement="bottom-end">
            <DropdownTrigger>
              <Avatar isBordered as="button" color="primary" size="sm" src={user.avatarUrl} />
            </DropdownTrigger>
            <DropdownMenu aria-label="Profile Actions" variant="flat">
              <DropdownItem key="home" href={PAGE_LIST.home}>
                Home
              </DropdownItem>
              <DropdownItem key="exam list" href={PAGE_LIST.exam.list}>
                Exam List
              </DropdownItem>
              <DropdownItem
                key="logout"
                className="text-danger"
                color="danger"
                onClick={() => logout()}
              >
                Log Out
              </DropdownItem>
            </DropdownMenu>
          </Dropdown>
        ) : (
          <NavbarItem>
            <Button onClick={handleGithubLogin}>Login</Button>
          </NavbarItem>
        )}
      </NavbarContent>
    </Navbar>
  );
};

export default EditorHeader;
