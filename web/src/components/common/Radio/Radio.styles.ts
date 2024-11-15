import styled from '@emotion/styled';
import { NormalColorType, PALETTE } from '../theme';

export const RadioPoint = styled.span`
  position: relative;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: ${PALETTE.gray};
  &::after {
    content: '';
    position: absolute;
    display: inline-block;
    background-color: ${PALETTE.white};
    width: 16px;
    height: 16px;
    transform: scale(1);
    border-radius: 50%;
  }
`;

export const RadioLabel = styled.label`
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
  &:hover ${RadioPoint}::after {
    background-color: ${PALETTE.gray};
    transition: 0.2s ease-in-out;
  }
`;

export const RadioText = styled.span<{
  labelColor: boolean;
  color: NormalColorType;
}>`
  font-size: 1rem;
  user-select: none;
  color: ${({ labelColor, color }) => labelColor && PALETTE[color]};
`;

export const RadioInput = styled.input<{ color: NormalColorType }>`
  display: none;

  // Switch Off
  & ~ ${RadioPoint} {
    background-color: ${PALETTE.gray};
  }

  // Switch On
  &:checked {
    & ~ ${RadioPoint} {
      background-color: ${({ color }) => PALETTE[color]};
      &::after {
        transform: scale(0.5);
      }
    }
  }
`;
