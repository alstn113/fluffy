import { SVGProps } from 'react';
const SvgUser = (props: SVGProps<SVGSVGElement>) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    data-name="Layer 1"
    viewBox="0 0 24 24"
    fill="none"
    {...props}
  >
    <path
      d="M16.043 14H7.957A4.963 4.963 0 0 0 3 18.957V24h18v-5.043A4.963 4.963 0 0 0 16.043 14Z"
      fill="currentColor"
    />
    <circle cx={12} cy={6} r={6} fill="currentColor" />
  </svg>
);
export default SvgUser;
