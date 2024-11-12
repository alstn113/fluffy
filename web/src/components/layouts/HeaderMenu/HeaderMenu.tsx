import { useRef } from 'react';
import useDisclosure from '~/hooks/useDisclosure';
import useOnClickOutside from '~/hooks/useOnClickOutside';
import * as S from './HeaderMenu.styles';
import { motion } from 'framer-motion';
import { Avatar } from '~/components/common';
import CaretDown from '~/components/vectors/CaretDown';
import useGetMe from '~/hooks/useGetMe';

interface HeaderDropdownProps {
  menuItemList: {
    text: string;
    onClick: () => void;
    red: boolean;
  }[];
}

const HeaderDropdown = ({ menuItemList }: HeaderDropdownProps) => {
  const { data: user } = useGetMe();

  const { isOpen, onClose, onToggle } = useDisclosure();
  const triggerRef = useRef<HTMLDivElement>(null);
  const contentRef = useRef<HTMLDivElement>(null);

  useOnClickOutside(triggerRef, (e) => {
    if (!contentRef.current?.contains(e.target as Node)) {
      onClose();
    }
  });

  return (
    <motion.nav initial={false} animate={isOpen ? 'open' : 'closed'}>
      <S.DropdownButton ref={triggerRef} onClick={onToggle}>
        <Avatar src={user?.avatarUrl || null} size="md" isBorder />
        <S.UserInfo>
          <div className="username">{user?.name}</div>
        </S.UserInfo>
        <CaretDown />
      </S.DropdownButton>
      <S.DropdownMenu
        variants={{
          open: {
            scale: 1,
            transition: {
              type: 'spring',
              bounce: 0,
              duration: 0.3,
              delayChildren: 0.2,
              staggerChildren: 0.05,
            },
          },
          closed: {
            scale: 0,
            transition: {
              type: 'spring',
              bounce: 0,
              duration: 0.2,
            },
          },
        }}
        ref={contentRef}
      >
        {menuItemList.map((item) => (
          <S.MenuItem
            key={item.text}
            onClick={item.onClick}
            red={item.red}
            variants={{
              open: {
                opacity: 1,
                y: 0,
                transition: { type: 'spring', stiffness: 300, damping: 24 },
              },
              closed: { opacity: 0, y: 20, transition: { duration: 0.2 } },
            }}
          >
            <S.MenuItemText>{item.text}</S.MenuItemText>
          </S.MenuItem>
        ))}
      </S.DropdownMenu>
    </motion.nav>
  );
};

export default HeaderDropdown;
