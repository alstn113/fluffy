import { Routes } from '@/constants';
import { NavbarBrand, Link } from '@nextui-org/react';

const NavbarLogo = () => {
  return (
    <NavbarBrand>
      <Link href={Routes.home()} color={'foreground'} className="flex items-center gap-2">
        <div
          className="font-bold text-2xl flex items-center gap-0.5"
          style={{
            fontFamily: 'Matemasie, sans-serif',
          }}
        >
          Fluffy.*
        </div>
      </Link>
    </NavbarBrand>
  );
};

export default NavbarLogo;
