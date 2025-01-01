import React from 'react';

interface CardDescriptionProps {
  children: React.ReactNode;
}

const CardDescription = ({ children }: CardDescriptionProps) => {
  return <p className="text-default-500 line-clamp-3 text-sm h-[3.75rem]">{children}</p>;
};

export default CardDescription;
