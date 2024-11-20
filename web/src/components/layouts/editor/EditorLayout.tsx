import EditorHeader from './EditorHeader';

interface EditorLayoutProps {
  children: React.ReactNode;
}

const EditorLayout = ({ children }: EditorLayoutProps) => {
  return (
    <div className="flex flex-col h-screen w-screen bg-white text-cyan-50">
      <EditorHeader />
      <div className="h-full w-full overflow-y-auto pb-14 pt-6 bg-gray-200">
        <main className="h-full w-full overflow-y-auto bg-white rounded-xl">{children}</main>
      </div>
    </div>
  );
};

export default EditorLayout;
