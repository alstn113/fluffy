import { NextUIProvider } from '@nextui-org/system';
import { useNavigate } from 'react-router';

export function Provider({ children }: { children: React.ReactNode }) {
  const navigate = useNavigate();

  return <NextUIProvider navigate={navigate}>{children}</NextUIProvider>;
}
