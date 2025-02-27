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
  NavbarContent,
  NavbarItem,
  useDisclosure,
} from '@heroui/react';
import { Routes } from '@/constants';
import useLogout from '@/hooks/useLogout.ts';
import { useNavigate } from 'react-router';
import useUser from '@/hooks/useUser.ts';
import { Avatar } from '@heroui/react';
import useCreateExam from '@/hooks/api/exam/useCreateExam';
import { useState } from 'react';
import NavbarLogo from './NavbarLogo';
import HeaderLoginButton from './HeaderLoginButton';

const Header = () => {
  const user = useUser();
  const logout = useLogout();
  const { mutate } = useCreateExam();
  const { isOpen, onOpen, onOpenChange, onClose } = useDisclosure();
  const [title, setTitle] = useState('');
  const navigate = useNavigate();

  const handleNewExam = () => {
    mutate(
      {
        title,
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
      <Navbar isBordered>
        <NavbarLogo />
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
                  홈
                </DropdownItem>
                <DropdownItem key="dashboard" href={Routes.dashboard()}>
                  대시보드
                </DropdownItem>
                <DropdownItem
                  key="new exam"
                  className="text-primary"
                  variant="faded"
                  color="primary"
                  onPress={onOpen}
                >
                  시험 생성
                </DropdownItem>
                <DropdownItem
                  key="logout"
                  className="text-danger"
                  color="danger"
                  onPress={() => logout()}
                >
                  로그아웃
                </DropdownItem>
              </DropdownMenu>
            </Dropdown>
          ) : (
            <HeaderLoginButton />
          )}
        </NavbarContent>
      </Navbar>
      <Modal isOpen={isOpen} onOpenChange={onOpenChange}>
        <ModalContent>
          {(onClose) => (
            <>
              <ModalHeader className="flex flex-col gap-1">시험 생성</ModalHeader>
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
