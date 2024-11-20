import EditorLayout from '@/components/layouts/editor/EditorLayout';

const AboutPage = () => {
  return (
    <EditorLayout>
      <div className="flex flex-row overflow-y-auto h-full w-full">
        <div className="flex flex-col h-full md:w-[300px] p-6 overflow-y-auto">a</div>
        <div className="flex grow flex-col h-full p-6 overflow-y-auto">a</div>
      </div>
    </EditorLayout>
  );
};

export default AboutPage;
