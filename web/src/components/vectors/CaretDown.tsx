import { SVGProps } from 'react';
const CaretDown = (props: SVGProps<SVGSVGElement>) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    id="Bold"
    viewBox="0 0 24 24"
    {...props}
  >
    <path
      d="M6.414,9H17.586a1,1,0,0,1,.707,1.707l-5.586,5.586a1,1,0,0,1-1.414,0L5.707,10.707A1,1,0,0,1,6.414,9Z"
      fill="currentColor"
    />
  </svg>
);
export default CaretDown;
