import { ExamAPI } from '@/api/examAPI';
import MDEditor from '@uiw/react-md-editor';
import rehypeSanitize from 'rehype-sanitize';

interface MarkdownEditorProps {
  examId: number;
  value: string;
  onChange: (value: string) => void;
}

const MarkdownEditor = ({ examId, value, onChange }: MarkdownEditorProps) => {
  return (
    <div>
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
          await onImagePasted(e.clipboardData, onChange, examId);
        }}
        onDrop={async (e) => {
          e.preventDefault();
          await onImagePasted(e.dataTransfer, onChange, examId);
        }}
      />
    </div>
  );
};

const onImagePasted = async (
  data: DataTransfer,
  onChange: (value: string) => void,
  examId: number,
) => {
  const images: File[] = [];
  for (let i = 0; i < data.items.length; i++) {
    const image = data.files.item(i);

    if (image) images.push(image);
  }

  await Promise.all(
    images.map(async (image) => {
      const { path } = await ExamAPI.uploadImage({ examId, image });
      const insertMarkdown = insertToTextArea(`![](${path})`);
      if (!insertMarkdown) return;

      onChange(insertMarkdown);
    }),
  );
};

const insertToTextArea = (text: string) => {
  const textarea = document.querySelector('textarea');
  if (!textarea) return;

  let sentence = textarea.value;
  const len = sentence.length;
  const pos = textarea.selectionStart;
  const end = textarea.selectionEnd;

  const front = sentence.slice(0, pos);
  const back = sentence.slice(pos, len);

  sentence = front + text + back;

  textarea.value = sentence;
  textarea.selectionEnd = end + text.length;

  return sentence;
};

export default MarkdownEditor;
