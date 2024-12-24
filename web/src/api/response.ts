export interface PageInfo {
  currentPage: number;
  totalPages: number;
  totalElements: number;
  hasNext: boolean;
  hasPrevious: boolean;
}

export interface PageResponse<T> {
  pageInfo: PageInfo;
  content: T[];
}
