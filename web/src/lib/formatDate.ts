import { formatDistanceToNow, format } from 'date-fns';
import { ko } from 'date-fns/locale';

const formatDate = (date: string | Date): string => {
  const d = date instanceof Date ? date : new Date(date);
  const now = Date.now();
  const diff = now - d.getTime();
  // less than 5 minutes
  if (diff < 1000 * 60 * 5) {
    return '방금 전';
  }

  // less than 1 hour
  if (diff < 1000 * 60 * 60 * 24) {
    return formatDistanceToNow(d, { addSuffix: true, locale: ko }); // 3시간 전
  }

  // less than 36 hours
  if (diff < 1000 * 60 * 60 * 36) {
    return '어제';
  }

  // less than 7 days
  if (diff < 1000 * 60 * 60 * 24 * 7) {
    return formatDistanceToNow(d, { addSuffix: true, locale: ko }); // 3일 전
  }

  return format(d, 'yyyy년 M월 d일'); // 2019년 1월 1일
};

export default formatDate;
