import { Routes } from '@/constants';
import { NavbarBrand, Link } from '@heroui/react';

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
          <div className="text-success-400">F</div>
          <div className="text-success-400">l</div>
          <div className="text-success-400">u</div>
          <div className="text-success-400">f</div>
          <div className="text-success-400">f</div>
          <div className="text-success-400">y</div>
          <div className="text-success-400">.</div>
          <div className="text-success-400">*</div>
        </div>
      </Link>
    </NavbarBrand>
  );
};

export default NavbarLogo;
