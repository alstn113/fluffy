import { NormalColorType } from '../theme';
import * as S from './Checkbox.styles';
import { forwardRef, InputHTMLAttributes } from 'react';

interface CheckboxProps extends InputHTMLAttributes<HTMLInputElement> {
  labelText?: string;
  color?: NormalColorType;
  noAnimation?: boolean; // 애니메이션 비활성화 prop 추가
}

export const Checkbox = forwardRef<HTMLInputElement, CheckboxProps>(function Checkbox(
  { labelText = '', color = 'primary', noAnimation = false, ...options },
  ref,
) {
  return (
    <S.CheckboxLabel>
      <S.CheckboxInput
        type="checkbox"
        ref={ref}
        color={color}
        noAnimation={noAnimation}
        {...options}
      />
      <S.CheckboxMask color={color} noAnimation={noAnimation}>
        <S.CheckIcon noAnimation={noAnimation} />
      </S.CheckboxMask>
      <S.CheckboxText>{labelText}</S.CheckboxText>
    </S.CheckboxLabel>
  );
});
