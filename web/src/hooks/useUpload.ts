import { useState, useCallback } from 'react';

const useUpload = () => {
  const [file, setFile] = useState<File | null>(null);

  const upload = useCallback((callback: (file: File | null) => void) => {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = 'image/*';

    input.onchange = () => {
      if (!input.files || input.files.length === 0) return;

      const selectedFile = input.files[0];
      setFile(selectedFile);
      callback(selectedFile);
    };

    input.click();
  }, []);

  return { upload, file };
};

export default useUpload;
