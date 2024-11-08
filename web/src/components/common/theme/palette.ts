export const PALETTE = {
  white: '#ffffff',
  gray: '#dfe6e9',
  black: '#000000',
  primary: '#0072F5',
  secondary: '#7828C8',
  success: '#17C964',
  warning: '#F5A524',
  error: '#F31260',
} as const;

export type NeutralColorType = keyof Pick<typeof PALETTE, 'white' | 'gray' | 'black'>;
export type NormalColorType = Exclude<keyof typeof PALETTE, NeutralColorType>;
