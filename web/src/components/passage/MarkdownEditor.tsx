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
  const files = data.files;
  if (!files) return;
  if (!files.item(0)) return;

  const image = files.item(0) as File;
  const imageName = image?.name || '이미지.png';

  const loadingText = `<!-- Uploading "${imageName}"... -->`;

  const insertMarkdown = insertToTextArea(loadingText);
  if (!insertMarkdown) return;
  onChange(insertMarkdown);

  await ExamAPI.uploadImage({ examId, image }).then(({ path }) => {
    const finalMarkdown = insertMarkdown.replace(loadingText, `![](${encodeURI(path)})`);
    onChange(finalMarkdown);
  });
};

const insertToTextArea = (intsertString: string) => {
  const textarea = document.querySelector('textarea');
  if (!textarea) return;

  let sentence = textarea.value;
  const len = sentence.length;
  const pos = textarea.selectionStart;
  const end = textarea.selectionEnd;

  const front = sentence.slice(0, pos);
  const back = sentence.slice(pos, len);

  sentence = front + intsertString + back;

  textarea.value = sentence;
  textarea.selectionEnd = end + intsertString.length;

  return sentence;
};

export default MarkdownEditor;
