import { NormalColorType } from '../theme';
import * as S from './Checkbox.styles';
import { forwardRef, InputHTMLAttributes } from 'react';

interface CheckboxProps extends InputHTMLAttributes<HTMLInputElement> {
  labelText?: string;
  color?: NormalColorType;
}

export const Checkbox = forwardRef<HTMLInputElement, CheckboxProps>(function Checkbox(
  { labelText = '', color = 'primary', ...options },
  ref,
) {
  return (
    <S.CheckboxLabel>
      <S.CheckboxInput type="checkbox" ref={ref} color={color} {...options} />
      <S.CheckboxMask color={color}>
        <S.CheckIcon />
      </S.CheckboxMask>
      <S.CheckboxText>{labelText}</S.CheckboxText>
    </S.CheckboxLabel>
  );
});
