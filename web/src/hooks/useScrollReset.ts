import { useEffect } from 'react';

const useScrollReset = () => {
  useEffect(() => {
    window.scrollTo(0, 0);
  }, []);
};

export default useScrollReset;
