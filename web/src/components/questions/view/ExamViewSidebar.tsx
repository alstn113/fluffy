import useExamEditorStore from '@/stores/useExamEditorStore';

const ExamViewSidebar = () => {
  const { questions, currentIndex, handleSelectQuestion } = useExamEditorStore();

  return (
    <div className="flex flex-col h-full md:w-[300px] p-6 border-border-divider border-r overflow-y-scroll">
      <div className="text-lg font-semibold mb-4">질문 목록</div>
      <div>
        {questions.map((item, index) => (
          <div key={index}>
            <div
              className={`flex items-center p-3 mb-2 rounded-md cursor-pointer ${
                currentIndex === index ? 'bg-gray-100' : 'bg-white'
              }`}
              onClick={() => handleSelectQuestion(index)}
            >
              <p
                className={`p-2 mr-2 text-small text-white rounded-md h-6 w-6 flex items-center justify-center ${
                  currentIndex === index ? 'bg-violet-600' : 'bg-gray-300'
                }`}
              >
                {index + 1}
              </p>
              <p className="truncate">{item.text}</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ExamViewSidebar;
