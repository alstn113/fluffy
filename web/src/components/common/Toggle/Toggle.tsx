import { forwardRef, InputHTMLAttributes } from 'react';
import * as S from './Toggle.styles';
import { NormalColorType } from '../theme';

export type ToggleVariantType = 'sm' | 'lg';

interface ToggleProps extends InputHTMLAttributes<HTMLInputElement> {
  labelText?: string;
  color?: NormalColorType;
  variant?: ToggleVariantType;
}

export const Toggle = forwardRef<HTMLInputElement, ToggleProps>(function Toggle(
  { labelText = '', color = 'primary', variant = 'sm', ...options },
  ref,
) {
  return (
    <S.ToggleLabel variant={variant}>
      <S.ToggleText variant={variant}>{labelText}</S.ToggleText>
      <S.ToggleCheckbox type="checkbox" variant={variant} ref={ref} color={color} {...options} />
      <S.ToggleSwitch variant={variant} />
    </S.ToggleLabel>
  );
});
