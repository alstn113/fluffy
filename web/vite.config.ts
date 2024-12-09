/// <reference types="vite/client" />

import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';
import tsconfigPaths from 'vite-tsconfig-paths';

// https://vitejs.dev/config/
export default ({ mode }: { mode: string }) => {
  process.env = { ...process.env, ...loadEnv(mode, process.cwd()) };

  const config = {
    plugins: [react(), tsconfigPaths()],
    server: {
      proxy: {
        '/api': {
          target: process.env.VITE_API_URL,
          changeOrigin: true,
          rewrite: (path: string) => path.replace(/^\/api/, ''),
        },
      },
    },
  };
  return defineConfig(config);
};
