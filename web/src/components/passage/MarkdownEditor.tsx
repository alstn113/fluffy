import { ExamAPI } from '@/api/examAPI';
import MDEditor from '@uiw/react-md-editor';
import rehypeSanitize from 'rehype-sanitize';
import ImageLoadingSpinner from './ImageLoadingSpinner';
import { FiPaperclip } from 'react-icons/fi';
import { useState } from 'react';
import useUpload from '@/hooks/useUpload';

interface MarkdownEditorProps {
  examId: number;
  value: string;
  onChange: (value: string) => void;
}

const MarkdownEditor = ({ examId, value, onChange }: MarkdownEditorProps) => {
  const noDragOverText = 'Paste, drop, or click to add files';
  const dragOverText = 'Drop to add files';

  const [dropText, setDropText] = useState(noDragOverText);
  const [isImageLoading, setIsImageLoading] = useState(false);

  const { upload } = useUpload();

  const handleUpload = async () => {
    upload(async (file) => {
      if (!file) return;
      setIsImageLoading(true);
      await handleImageUpload(file);
      setIsImageLoading(false);
    });
  };

  const handlePasteOrDrop = async (data: DataTransfer) => {
    const files = data.files;
    if (!files || !files.length) return;

    const image = files.item(0) as File;
    await handleImageUpload(image);
  };

  const handleImageUpload = async (image: File) => {
    const imageName = image?.name || '이미지.png';
    const loadingText = `<!-- Uploading "${imageName}"... -->`;

    const insertMarkdown = insertToTextArea(loadingText);
    if (!insertMarkdown) return;
    onChange(insertMarkdown);

    const { path } = await ExamAPI.uploadImage({ examId, image });
    const finalMarkdown = insertMarkdown.replace(loadingText, `![](${encodeURI(path)})`);
    onChange(finalMarkdown);
  };

  return (
    <div className="mt-4">
      <MDEditor
        value={value}
        onChange={(v) => onChange(v!)}
        textareaProps={{
          placeholder: '지문이 필요한 경우 입력하세요... (선택사항)',
        }}
        previewOptions={{
          rehypePlugins: [[rehypeSanitize]],
        }}
        onPaste={async (e) => {
          e.preventDefault();
          setIsImageLoading(true);
          await handlePasteOrDrop(e.clipboardData);
          setIsImageLoading(false);
        }}
        onDrop={async (e) => {
          e.preventDefault();
          setIsImageLoading(true);
          setDropText(noDragOverText);
          await handlePasteOrDrop(e.dataTransfer);
          setIsImageLoading(false);
        }}
        onDragOver={(e) => {
          e.preventDefault();
          setDropText(dragOverText);
        }}
        onDragLeave={(e) => {
          e.preventDefault();
          setDropText(noDragOverText);
        }}
      />
      <div
        className="w-full mt-2 hover:bg-gray-100 p-3 rounded-md cursor-pointer transition-colors"
        onClick={handleUpload}
      >
        <div className="flex text-gray-500 text-sm">
          <div className="flex items-center justify-center gap-2">
            {isImageLoading ? <ImageLoadingSpinner /> : <FiPaperclip size={20} />}
            <div>{dropText}</div>
          </div>
        </div>
      </div>
    </div>
  );
};

const insertToTextArea = (insertString: string) => {
  const textarea = document.querySelector('textarea');
  if (!textarea) return;

  const sentence = textarea.value;
  const pos = textarea.selectionStart;
  const end = textarea.selectionEnd;

  const updatedSentence = sentence.slice(0, pos) + insertString + sentence.slice(pos);

  textarea.value = updatedSentence;
  textarea.selectionEnd = end + insertString.length;

  return updatedSentence;
};

export default MarkdownEditor;
