import { css } from '@emotion/react';
import styled from '@emotion/styled';
import type { AvatarSize } from './Avatar';

export const Container = styled.div<{ size: AvatarSize; isBorder: boolean }>`
  // md
  width: 32px;
  height: 32px;
  border-radius: 50%;
  overflow: hidden;

  ${({ isBorder }) =>
    isBorder &&
    css`
      border: 2px solid #0072f5;
    `}

  ${({ size }) =>
    size === 'sm' &&
    css`
      width: 24px;
      height: 24px;
    `}

  ${({ size }) =>
    size === 'lg' &&
    css`
      width: 40px;
      height: 40px;
    `}

  ${({ size }) =>
    size === 'xl' &&
    css`
      width: 128px;
      height: 128px;
    `}
`;

export const Image = styled.img`
  object-fit: cover;
  width: 100%;
  height: 100%;
`;
