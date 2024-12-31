import useGetMySubmissionSummaries from '@/hooks/api/submission/useGetMySubmissionSummaries';
import { fullDate } from '@/lib/date';
import { useEffect } from 'react';
import AsyncBoundary from '../AsyncBoundary';
import SubmissionWithAnswerViewPanel from './SubmissionWithAnswerViewPanel';

interface ExamSubmissionsViewContentProps {
  examId: number;
  current: { currentIndex: number; currentSubmissionId: number | null };
  onChangeCurrent: (current: { currentIndex: number; currentSubmissionId: number }) => void;
}

const ExamSubmissionsViewContent = ({
  examId,
  current,
  onChangeCurrent,
}: ExamSubmissionsViewContentProps) => {
  const { data: submissions } = useGetMySubmissionSummaries(examId);

  useEffect(() => {
    if (submissions && submissions.length > 0 && current.currentSubmissionId === null) {
      onChangeCurrent({
        currentIndex: 0,
        currentSubmissionId: submissions[0].submissionId,
      });
    }
  }, [submissions, current.currentSubmissionId, onChangeCurrent]);

  const handleChangeCurrent = (nextIndex: number, nextSubmissionId: number) => {
    onChangeCurrent({ currentIndex: nextIndex, currentSubmissionId: nextSubmissionId });
  };

  return (
    <>
      <div className="flex flex-col h-full md:w-[300px] p-6 border-border-divider border-r overflow-y-scroll">
        <div className="text-lg font-semibold mb-4">제출 목록</div>
        <div>
          {submissions?.map((item, index) => (
            <div key={index}>
              <div
                className={`flex items-center p-3 mb-2 rounded-md cursor-pointer ${
                  current.currentIndex === index ? 'bg-gray-100' : 'bg-white'
                }`}
                onClick={() => handleChangeCurrent(index, item.submissionId)}
              >
                <p
                  className={`p-2 mr-2 text-small text-white rounded-md h-6 w-6 flex items-center justify-center ${
                    current.currentIndex === index ? 'bg-violet-600' : 'bg-gray-300'
                  }`}
                >
                  {index + 1}
                </p>
                <p className="truncate">{fullDate(item.submittedAt)}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
      <div className="flex grow flex-col h-full p-6 overflow-y-auto items-center">
        {current.currentSubmissionId && (
          <AsyncBoundary>
            <SubmissionWithAnswerViewPanel
              examId={examId}
              submissionId={current.currentSubmissionId}
            />
          </AsyncBoundary>
        )}
      </div>
    </>
  );
};

export default ExamSubmissionsViewContent;
