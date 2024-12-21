import NavbarLogo from '@/components/layouts/base/NavbarLogo';
import { Navbar, NavbarContent } from '@nextui-org/react';
import { FaArrowLeft } from 'react-icons/fa';
import { Routes } from '@/constants';
import { Link } from 'react-router';

const ExamProgressHeader = () => {
  return (
    <>
      <Navbar isBordered>
        <NavbarContent justify="start">
          <Link to={Routes.home()} className="flex items-center gap-2">
            <FaArrowLeft />
          </Link>
        </NavbarContent>
        <NavbarContent justify="center">
          <NavbarLogo />
        </NavbarContent>
        <NavbarContent justify="end"></NavbarContent>
      </Navbar>
    </>
  );
};

export default ExamProgressHeader;
