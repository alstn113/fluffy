import { CardBody } from '@heroui/react';

interface BaseCardBodyProps {
  title: string;
  description: string;
  children?: React.ReactNode;
}

const BaseCardBody = ({ title, description, children }: BaseCardBodyProps) => {
  return (
    <CardBody>
      <div className="text-lg font-semibold">{title}</div>
      <p className="text-default-500 line-clamp-3 text-sm h-[3.75rem]">{description}</p>
      {children}
    </CardBody>
  );
};

export default BaseCardBody;
