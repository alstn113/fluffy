import styled from '@emotion/styled';
import { NormalColorType, PALETTE } from '../theme';

export const CheckboxText = styled.span`
  font-size: 1rem;
  user-select: none;
`;

export const CheckboxMask = styled.span<{ noAnimation: boolean }>`
  box-sizing: border-box;
  position: relative;
  width: 24px;
  height: 24px;
  border-radius: 33%;
  border: 3px solid ${PALETTE.gray};
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: ${PALETTE.white};

  &::before {
    content: '';
    position: absolute;
    display: inline-block;
    width: 24px;
    height: 24px;
    transform: scale(0.3);
    opacity: 0;
    border-radius: 33%;
    transition: ${({ noAnimation }) =>
      noAnimation
        ? 'none'
        : 'background-color 0.2s ease-in-out, transform 0.2s ease-in-out, opacity 0.2s ease-in-out'};
  }
`;

export const CheckboxLabel = styled.label`
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
  &:hover ${CheckboxMask} {
    background-color: ${PALETTE.gray};
  }
`;

export const CheckIcon = styled.i<{ noAnimation: boolean }>`
  width: 8px;
  height: 14px;
  display: block;
  position: relative;
  margin-top: -4px;
  transform: rotate(45deg) scale(0.8);
  transition: ${({ noAnimation }) => (noAnimation ? 'none' : 'opacity 0.2s ease')};

  &::before {
    content: '';
    position: absolute;
    width: 0px;
    height: 3px;
    border-radius: 5px 0px 0px 5px;
    bottom: 0px;
    background-color: ${PALETTE.white};
    transition: ${({ noAnimation }) => (noAnimation ? 'none' : 'width 0.2s ease')};
  }

  &::after {
    content: '';
    position: absolute;
    height: 0px;
    border-radius: 5px 5px 0px 0px;
    bottom: 0px;
    right: 0px;
    width: 3px;
    background-color: ${PALETTE.white};
    transition: ${({ noAnimation }) => (noAnimation ? 'none' : 'height 0.2s ease 0.2s')};
  }
`;

export const CheckboxInput = styled.input<{ color: NormalColorType; noAnimation: boolean }>`
  display: none;

  // Switch Off
  & ~ ${CheckboxMask} {
    ${CheckIcon} {
      opacity: 0;
    }
  }

  // Switch On
  &:checked {
    & ~ ${CheckboxMask} {
      &::before {
        background-color: ${({ color }) => PALETTE[color]};
        transform: scale(1);
        opacity: 1;
      }
      ${CheckIcon} {
        opacity: 1;
        &::before {
          width: 8px;
        }
        &::after {
          height: 13px;
        }
      }
    }
  }
`;
