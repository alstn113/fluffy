import * as S from './Avatar.styles';

export type AvatarSize = 'sm' | 'md' | 'lg' | 'xl';

export interface AvatarProps {
  src: string | null;
  alt?: string;
  size?: AvatarSize;
  isBorder?: boolean;
}

export const Avatar = ({
  src,
  alt = 'profile image',
  size = 'md',
  isBorder = false,
}: AvatarProps) => {
  const imageSrc = src || 'BlankUser';
  return (
    <S.Container size={size} isBorder={isBorder}>
      {src ? <S.Image src={imageSrc} alt={alt} /> : null}
    </S.Container>
  );
};

export default Avatar;
