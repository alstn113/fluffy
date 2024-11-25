import React, { Suspense } from 'react';
import { ErrorBoundary } from 'react-error-boundary';
import { QueryErrorResetBoundary } from '@tanstack/react-query';
import ErrorFallback from './ErrorFallback';
import { Spinner } from '@nextui-org/react';

interface SuspenseErrorBoundaryProps {
  children: React.ReactNode;
  ErrorFallbackComponent?: React.ComponentType<{ error: Error; resetErrorBoundary: () => void }>;
}

const AsyncBoundary = ({ children, ErrorFallbackComponent }: SuspenseErrorBoundaryProps) => {
  return (
    <QueryErrorResetBoundary>
      {({ reset }) => (
        <ErrorBoundary
          onReset={reset}
          fallbackRender={({ error, resetErrorBoundary }) =>
            ErrorFallbackComponent ? (
              <ErrorFallbackComponent error={error} resetErrorBoundary={resetErrorBoundary} />
            ) : (
              <ErrorFallback error={error} resetErrorBoundary={resetErrorBoundary} />
            )
          }
        >
          <Suspense
            fallback={
              <div className="flex items-center justify-center w-full h-full">
                <Spinner size="lg" />
              </div>
            }
          >
            {children}
          </Suspense>
        </ErrorBoundary>
      )}
    </QueryErrorResetBoundary>
  );
};

export default AsyncBoundary;
