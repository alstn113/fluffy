import { createRoot } from 'react-dom/client';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import App from './App';
import { BrowserRouter } from 'react-router';
import AsyncBoundary from './components/AsyncBoundary.tsx';
import { Toaster } from 'sonner';

import '@/styles/global.css';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: false,
      refetchOnWindowFocus: false,
    },
  },
});

createRoot(document.getElementById('root')!).render(
  <AsyncBoundary>
    <QueryClientProvider client={queryClient}>
      <ReactQueryDevtools initialIsOpen={false} />
      <BrowserRouter>
        <App />
        <Toaster richColors />
      </BrowserRouter>
    </QueryClientProvider>
  </AsyncBoundary>,
);
