import { css } from '@emotion/react';
import styled from '@emotion/styled';
import { motion } from 'framer-motion';

export const DropdownButton = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  color: #888f96;
  cursor: pointer;
  transition: 0.2s ease-in-out;
  svg {
    margin-right: 8px;
    width: 16px;
    height: 16px;
  }
  &:hover {
    color: #c3ced9;
  }
`;
export const DropdownMenu = styled(motion.div)`
  position: absolute;
  top: 60px;
  right: 10px;
  // 기준점
  transform-origin: 70% top;
  margin-top: 0.5rem;
  padding: 8px;
  background: #26292b;
  border-radius: 14px;
  z-index: 20;
`;
export const UserInfo = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
  margin: 0 8px;
  .username {
    font-size: 14px;
    font-weight: 500;
    color: #000000;
  }
`;
export const MenuItem = styled(motion.div)<{ red?: boolean }>`
  display: flex;
  flex-direction: column;
  min-width: 200px;
  height: 36px;
  padding: 0 12px;
  justify-content: center;
  border-radius: 9px;
  cursor: pointer;
  &:hover {
    background-color: #495057;
  }
  transition: background-color 0.2s;
  ${({ red }) =>
    red &&
    css`
      background-color: #3f0b1f;
      &:hover {
        background-color: #300313;
      }
      span {
        color: #f4256d;
      }
    `}
`;
export const MenuItemText = styled.span`
  flex: 1 1 0%;
  font-size: 16px;
  text-align: left;
  line-height: 36px;
  height: 36px;
  border-radius: 14px;
  color: #fff;
`;
