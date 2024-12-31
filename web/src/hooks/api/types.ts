import { UseMutationOptions, UseQueryOptions } from '@tanstack/react-query';

export type UseQueryOptionsOf<T extends (...args: any) => any, E = any> = Omit<
  UseQueryOptions<Awaited<ReturnType<T>>, E, Awaited<ReturnType<T>>, any[]>,
  'queryKey' | 'queryFn'
>;

export type UseMutationOptionsOf<T extends (...args: any) => any, E = any> = Omit<
  UseMutationOptions<
    Awaited<ReturnType<T>>,
    E,
    Parameters<T>[0] extends undefined ? void : Parameters<T>[0]
  >,
  'mutationFn'
>;
