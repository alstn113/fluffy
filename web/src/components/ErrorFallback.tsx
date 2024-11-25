import { AxiosError } from 'axios';

interface ErrorFallbackProps {
  error: AxiosError;
  resetErrorBoundary: () => void;
}

const ErrorFallback = ({ error, resetErrorBoundary }: ErrorFallbackProps) => {
  const status = error.response?.status;

  const message = (stauts: number) => {
    switch (stauts) {
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
      <button
        onClick={resetErrorBoundary}
        className="mt-4 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition duration-200"
      >
        다시 시도
      </button>
    </div>
  );
};

export default ErrorFallback;
