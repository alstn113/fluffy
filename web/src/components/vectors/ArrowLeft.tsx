import { SVGProps } from 'react';
const SvgArrowLeft = (props: SVGProps<SVGSVGElement>) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    viewBox="0 0 24 24"
    fill="none"
    {...props}
  >
    <path
      d="M19 10.5h-8.793l2.439-2.439a1.5 1.5 0 0 0-2.121-2.122L6.939 9.525a3.505 3.505 0 0 0 0 4.95l3.586 3.586a1.5 1.5 0 0 0 2.121-2.122L10.207 13.5H19a1.5 1.5 0 0 0 0-3Z"
      fill="currentColor"
    />
  </svg>
);
export default SvgArrowLeft;
