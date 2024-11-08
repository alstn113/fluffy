import { NormalColorType } from '../theme';
import { forwardRef, InputHTMLAttributes } from 'react';
import * as S from './Radio.styles';

interface RadioProps extends InputHTMLAttributes<HTMLInputElement> {
  labelText?: string;
  color?: NormalColorType;
  labelColor?: boolean;
}

export const Radio = forwardRef<HTMLInputElement, RadioProps>(function Radio(
  { labelText = '', labelColor = false, color = 'primary', ...options },
  ref,
) {
  return (
    <S.RadioLabel>
      <S.RadioInput type="radio" ref={ref} color={color} {...options} />
      <S.RadioPoint color={color} />
      <S.RadioText labelColor={labelColor} color={color}>
        {labelText}
      </S.RadioText>
    </S.RadioLabel>
  );
});
