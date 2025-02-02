export interface MarkdownEditorProps {
  onChangeMarkdown: (markdown: string) => void;
  onChangeTitle: (title: string) => void;
  onConvert: (markdown: string) => void;
  title: string;
  markdown: string;
  tagInput: React.ReactNode;
  footer: React.ReactNode;
  onUpload: () => void; // 이미지를 열어서 업로드하는 함수
  lastUploadedImage: string | null;
  initialBody: string;
  theme: 'light' | 'dark';
  tempBlobImage: string | null;
}

type MarkdownEditorState = {
  addLink: {
    top: number | null;
    bottom: number | null;
    left: number;
    visible: boolean;
    stickToRight: boolean;
  };
  askChangeEditor: boolean;
  clientWidth: number;
  hideUpper: boolean;
};

export default class WriteMarkdownEditor extends React.Component<
  MarkdownEditorProps,
  MarkdownEditorState
> {
  block = React.createRef<HTMLDivElement>();
  toolbarElement = React.createRef<HTMLDivElement>();
  editorElement = React.createRef<HTMLTextAreaElement>();

  toolbarTop = 0;
  state = {
    addLink: {
      top: 0,
      bottom: 0,
      left: 0,
      visible: false,
      stickToRight: false,
    },
    askChangeEditor: false,
    clientWidth: 0,
    hideUpper: false,
  };
  codemirror: EditorFromTextArea | null = null;
  ignore = false;
  isIOS = detectIOS();

  initialize = () => {
    this.codemirror.on('dragover', (cm, e) => {
      if (e.dataTransfer) {
        e.dataTransfer.dropEffect = 'copy';
      }
    });

  addImageToEditor = (image: string) => {
    if (!this.codemirror) return;
    const lines = this.codemirror.getValue().split('\n');
    const lineIndex = lines.findIndex((l) => l.includes('![업로드중..]'));
    if (lineIndex === -1) return;

    const startCh = lines[lineIndex].indexOf('![업로드중..]');
    this.codemirror
      .getDoc()
      .replaceRange(
        `![](${encodeURI(image)})`,
        { line: lineIndex, ch: startCh },
        { line: lineIndex, ch: lines[lineIndex].length },
      );
    // this.codemirror.getDoc().replaceSelection(`![](${encodeURI(image)})`);
  };

  addTempImageBlobToEditor = (blobUrl: string) => {
    const imageMarkdown = `![업로드중..](${blobUrl})\n`;

    this.codemirror.getDoc().replaceSelection(imageMarkdown);
  };

  componentDidUpdate(prevProps: MarkdownEditorProps) {
    const { lastUploadedImage, initialBody } = this.props;
    if (initialBody !== prevProps.initialBody) {
      if (!this.codemirror) return;
      if (this.codemirror.getValue() === this.props.initialBody) return;
      this.codemirror.setValue(this.props.initialBody);
    }
    if (lastUploadedImage && prevProps.lastUploadedImage !== lastUploadedImage) {
      this.addImageToEditor(lastUploadedImage);
    }

    if (this.props.tempBlobImage && this.props.tempBlobImage !== prevProps.tempBlobImage) {
      this.addTempImageBlobToEditor(this.props.tempBlobImage);
    }
  }

  public render() {
    const { addLink, clientWidth } = this.state;
    const { title, tagInput, footer } = this.props;
    return (
      <MarkdownEditorBlock ref={this.block} data-testid="codemirror">
        <div className="wrapper">
          <WriterHead hide={this.state.hideUpper}>
            {ssrEnabled ? (
              <TitleTextareaForSSR placeholder="제목을 입력하세요" rows={1} />
            ) : (
              <TitleTextarea
                placeholder="제목을 입력하세요"
                onChange={this.handleTitleChange}
                value={title}
              />
            )}
            <HorizontalBar />
            {tagInput}
          </WriterHead>
          <Toolbar
            shadow={this.state.hideUpper}
            mode="MARKDOWN"
            onClick={this.handleToolbarClick}
            onConvert={this.handleAskConvert}
            innerRef={this.toolbarElement}
            ios={this.isIOS}
          />
          <MarkdownWrapper>
            {addLink.visible && (
              <AddLink
                defaultValue=""
                left={addLink.left}
                top={addLink.top}
                bottom={addLink.bottom}
                stickToRight={addLink.stickToRight}
                onConfirm={this.handleConfirmAddLink}
                onClose={this.handleCancelAddLink}
              />
            )}
            <textarea ref={this.editorElement} style={{ display: 'none' }} />
          </MarkdownWrapper>
        </div>
        <AskChangeEditor
          visible={this.state.askChangeEditor}
          onConfirm={this.handleConfirmConvert}
          onCancel={this.handleCancelConvert}
          convertTo={WriteMode.WYSIWYG}
        />
        <FooterWrapper style={{ width: clientWidth || '50%' }}>{footer}</FooterWrapper>
      </MarkdownEditorBlock>
    );
  }
}
