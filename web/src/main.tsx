import { createRoot } from 'react-dom/client';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import App from './App';
import { Provider } from './provider.tsx';
import '@/styles/global.css';
import { BrowserRouter } from 'react-router-dom';
import AsyncBoundary from './components/AsyncBoundary.tsx';

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
        <Provider>
          <App />
        </Provider>
      </BrowserRouter>
    </QueryClientProvider>
  </AsyncBoundary>,
);
