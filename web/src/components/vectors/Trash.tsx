import { SVGProps } from 'react';
const SvgTrash = (props: SVGProps<SVGSVGElement>) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    data-name="Isolation Mode"
    viewBox="0 0 24 24"
    fill="none"
    {...props}
  >
    <path
      d="M23 3h-5v-.5A2.5 2.5 0 0 0 15.5 0h-7A2.5 2.5 0 0 0 6 2.5V3H1v3h2v15a3 3 0 0 0 3 3h12a3 3 0 0 0 3-3V6h2Zm-5 18H6V6h12Z"
      fill="currentColor"
    />
    <path d="M8 9h3v9H8zM13 9h3v9h-3z" fill="currentColor" />
  </svg>
);
export default SvgTrash;
