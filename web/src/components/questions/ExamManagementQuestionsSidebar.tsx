import ExamEditorSidebar from './editor/ExamEditorSidebar';
import ExamViewSidebar from './view/ExamViewSidebar';

interface ExamManagementQuestionSidebarProps {
  isPublished: boolean;
}

const ExamManagementQuestionsSidebar = ({ isPublished }: ExamManagementQuestionSidebarProps) => {
  return isPublished ? <ExamViewSidebar /> : <ExamEditorSidebar />;
};

export default ExamManagementQuestionsSidebar;
