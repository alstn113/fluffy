import { useRef } from 'react';
import { AnimatePresence, motion } from 'framer-motion';

import { BsThreeDotsVertical as MenuDots } from 'react-icons/bs';
import { useDisclosure } from '@heroui/react';
import useOnClickOutside from '@/hooks/useOnClickOutside';

interface MoreVertMenuItem {
  icon: React.ReactNode;
  name: string;
  onClick: () => void;
}

interface MoreVertMenuProps {
  items: MoreVertMenuItem[];
}

const MoreVertMenu = ({ items }: MoreVertMenuProps) => {
  const { isOpen, onClose, onOpen } = useDisclosure();
  const triggerRef = useRef<HTMLButtonElement>(null);

  useOnClickOutside(triggerRef, onClose);

  const handleButtonClick = () => {
    if (isOpen) {
      onClose();
    } else {
      onOpen();
    }
  };

  return (
    <div className="relative">
      <button
        ref={triggerRef}
        onClick={handleButtonClick}
        className="flex items-center justify-center w-8 h-8 p-0 text-gray-700 rounded-full hover:bg-gray-100 transition-colors duration-0.2"
      >
        <MenuDots className="w-5 h-5" />
      </button>

      <AnimatePresence initial={false}>
        {isOpen && (
          <motion.div
            initial={{ scale: 0.9, opacity: 0.2 }}
            animate={{ scale: 1, opacity: 1 }}
            exit={{ scale: 0.9, opacity: 0 }}
            transition={{ type: 'spring', bounce: 0.3, duration: 0.3 }}
            className="absolute top-full right-0 mt-2 w-[100px] bg-white text-gray-800 rounded-lg shadow-lg z-50 p-2 border border-gray-200"
            style={{ transformOrigin: 'right top' }}
          >
            {items.map((item) => (
              <div
                key={item.name}
                onClick={item.onClick}
                className="flex items-center gap-2 p-2 rounded-md cursor-pointer hover:bg-gray-100 transition-colors duration-0.2"
              >
                <span className="flex items-center gap-2 text-sm">
                  {item.icon}
                  {item.name}
                </span>
              </div>
            ))}
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
};

export default MoreVertMenu;
