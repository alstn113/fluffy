// vercel
import { SpeedInsights } from '@vercel/speed-insights/react';
import { Analytics } from '@vercel/analytics/react';

import { createRoot } from 'react-dom/client';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import App from './App';
import '@/styles/global.css';
import { BrowserRouter } from 'react-router';
import AsyncBoundary from './components/AsyncBoundary.tsx';
import { Toaster } from 'react-hot-toast';

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
        <SpeedInsights />
        <Analytics />
        <Toaster />
      </BrowserRouter>
    </QueryClientProvider>
  </AsyncBoundary>,
);
