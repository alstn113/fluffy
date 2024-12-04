import {
  Button,
  Dropdown,
  DropdownItem,
  DropdownMenu,
  DropdownTrigger,
  Input,
  Link,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  Navbar,
  NavbarBrand,
  NavbarContent,
  NavbarItem,
  useDisclosure,
} from '@nextui-org/react';
import { GITHUB_OAUTH_LOGIN_URL, Routes } from '@/constants';
import useLogout from '@/hooks/useLogout.ts';
import { useLocation, useNavigate } from 'react-router';
import useUser from '@/hooks/useUser.ts';
import { Avatar } from '@daveyplate/nextui-fixed-avatar';
import useCreateExam from '@/hooks/api/exam/useCreateExam';
import { useState } from 'react';

const Header = () => {
  const user = useUser();
  const logout = useLogout();
  const location = useLocation();
  const { mutate } = useCreateExam();
  const { isOpen, onOpen, onOpenChange, onClose } = useDisclosure();
  const [title, setTitle] = useState('');
  const navigate = useNavigate();

  const handleGithubLogin = () => {
    window.location.href = `${GITHUB_OAUTH_LOGIN_URL}?next=${location.pathname}`;
  };

  const handleNewExam = () => {
    mutate(
      {
        title: 'New Exam',
      },
      {
        onSuccess: (data) => {
          const examId = data.id;
          navigate(Routes.exam.management.questions(examId));
        },
        onSettled: () => {
          onClose();
        },
      },
    );
  };

  return (
    <>
      <Navbar>
        <NavbarBrand>
          <Link href={Routes.home()} color={'foreground'}>
            <p className="font-bold text-inherit">Pass</p>
          </Link>
        </NavbarBrand>
        <NavbarContent className="hidden sm:flex gap-4" justify="center">
          <NavbarItem>
            <Link color="foreground" href={Routes.about()}>
              About
            </Link>
          </NavbarItem>
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
                  key="new exam"
                  className="text-primary"
                  variant="faded"
                  color="primary"
                  onPress={onOpen}
                >
                  New Exam
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
      <Modal isOpen={isOpen} onOpenChange={onOpenChange}>
        <ModalContent>
          {(onClose) => (
            <>
              <ModalHeader className="flex flex-col gap-1">새로운 시험 생성</ModalHeader>
              <ModalBody>
                <Input label="시험 제목" value={title} onValueChange={setTitle} />
              </ModalBody>
              <ModalFooter>
                <Button color="danger" variant="light" onPress={onClose}>
                  닫기
                </Button>
                <Button color="primary" onPress={handleNewExam}>
                  확인
                </Button>
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
    </>
  );
};

export default Header;
