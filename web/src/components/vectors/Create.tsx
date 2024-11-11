import { SVGProps } from 'react';
const SvgCreate = (props: SVGProps<SVGSVGElement>) => (
  <svg
    viewBox="0 0 24 24"
    xmlns="http://www.w3.org/2000/svg"
    data-name="Layer 1"
    {...props}
  >
    <path
      d="M12 0a12 12 0 1 0 12 12A12.013 12.013 0 0 0 12 0zm4 13h-3v3a1 1 0 0 1-2 0v-3H8a1 1 0 0 1 0-2h3V8a1 1 0 0 1 2 0v3h3a1 1 0 0 1 0 2z"
      fill="currentColor"
    />
  </svg>
);
export default SvgCreate;
