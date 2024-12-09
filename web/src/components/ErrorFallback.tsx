import { AxiosError } from 'axios';
import { Button } from '@nextui-org/react';
import { useNavigate } from 'react-router';
import { Routes } from '@/constants';

interface ErrorFallbackProps {
  error: AxiosError;
  resetErrorBoundary: () => void;
}

const ErrorFallback = ({ error, resetErrorBoundary }: ErrorFallbackProps) => {
  const status = error.response?.status;
  const navigate = useNavigate();

  const message = (status: number) => {
    switch (status) {
      case 400:
        return '400: 잘못된 요청입니다.';
      case 401:
        return '401: 인증이 필요합니다.';
      case 403:
        return '403: 권한이 없습니다.';
      case 404:
        return '404: 요청한 리소스를 찾을 수 없습니다.';
      default:
        return '알 수 없는 에러가 발생했습니다.';
    }
  };

  return (
    <div className="flex flex-col items-center justify-center gap-4 h-full w-full">
      <h1 className="text-4xl font-bold text-gray-800">Error</h1>
      <p className="text-lg text-gray-600 mt-4">{message(status!)}</p>
      <div className={'flex gap-4'}>
        <Button onPress={resetErrorBoundary} variant={'shadow'} className="mt-4 px-4 py-2" color="primary">
          다시 시도
        </Button>
        <Button
          onPress={() => navigate(Routes.home())}
          variant={'shadow'}
          className="mt-4 px-4 py-2 text-white"
          color="success"
        >
          홈으로
        </Button>
      </div>
    </div>
  );
};

export default ErrorFallback;
