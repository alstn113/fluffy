import { NormalColorType } from '../theme';
import { forwardRef, InputHTMLAttributes } from 'react';
import * as S from './Radio.styles';

interface RadioProps extends InputHTMLAttributes<HTMLInputElement> {
  labelText?: string;
  color?: NormalColorType;
  labelColor?: boolean;
  noAnimation?: boolean; // 애니메이션 비활성화 prop 추가
}

export const Radio = forwardRef<HTMLInputElement, RadioProps>(function Radio(
  { labelText = '', labelColor = false, color = 'primary', noAnimation = false, ...options },
  ref,
) {
  return (
    <S.RadioLabel>
      <S.RadioInput type="radio" ref={ref} color={color} noAnimation={noAnimation} {...options} />
      <S.RadioPoint color={color} noAnimation={noAnimation} />
      <S.RadioText labelColor={labelColor} color={color}>
        {labelText}
      </S.RadioText>
    </S.RadioLabel>
  );
});
