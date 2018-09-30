package io.orbit.webtools.css;

import io.orbit.api.EditorController;
import io.orbit.api.text.CodeEditor;
import io.orbit.api.autocompletion.AutoCompletionDialog;
import io.orbit.settings.LocalUser;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.reactfx.EventStreams;

import java.io.File;
import java.util.List;

/**
 * Created by Tyler Swann on Sunday April 01, 2018 at 14:39
 */
public class CSSEditorController implements EditorController
{
    private CSS3AutoCompletionProvider provider = new CSS3AutoCompletionProvider();
    private AutoCompletionDialog<CSS3AutoCompletionOption> dialog;
    private CodeEditor editor;
    private CSS3AutoCompletionOption currentOption;
    private boolean isHotKeyPress = false;

    @Override
    public void start(File file, CodeEditor editor)
    {
        new CSS3Formatter(editor).play();
        this.dialog = new AutoCompletionDialog<>(editor);
        this.editor = editor;
        registerListeners();
    }

    private void registerListeners()
    {
        this.dialog.setCellFactory(cssOption -> cssOption.text);
        this.editor.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> this.dialog.hide());
        this.dialog.selectedOptionProperty().addListener(event -> this.dialog.getSelectedOption().ifPresent(option -> {
            int lineNumber = this.editor.getFocusPosition().line;
            int characterNumber = this.editor.getFocusPosition().caretPositionInDocument;
            String currentLine = this.editor.getIndexedDocument().lines.get(lineNumber).text.replaceAll("^\\s*", "");
            if (this.currentOption == null)
            {
                String textFragment = option.insertedText.replaceFirst(currentLine, "");
                this.editor.insertText(characterNumber, textFragment);
            }
            else
            {
                currentLine = currentLine.replaceFirst(this.currentOption.insertedText, "");
                String textFragment = option.insertedText.replaceFirst(currentLine, "");
                this.editor.insertText(characterNumber, textFragment);
                this.currentOption = null;
                this.dialog.hide();
            }
            if (option.subOptions.size() > 0)
            {
                this.currentOption = option;
                this.dialog.updateOptions(option.subOptions);
                updateDialogPosition();
            }
        }));
        this.editor.addEventHandler(KeyEvent.KEY_PRESSED, event -> isHotKeyPress = LocalUser.settings.getHotKeys().isHotKeyEvent(event));
        this.editor.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (isHotKeyPress)
                return;
            int lineNumber = this.editor.getFocusPosition().line;
            String lineText = this.editor.getIndexedDocument().lines.get(lineNumber).text;
            switch (event.getCode())
            {
                case ENTER: break;
                case BACK_SPACE:
                case COLON:
                case END:
                case HOME:
                    this.dialog.hide();
                    break;
                default:
                    if (event.getCode().isArrowKey())
                        break;
                    List<CSS3AutoCompletionOption> options;
                    if (this.currentOption == null)
                        options = this.provider.optionsForLine(lineText);
                    else
                        options = this.provider.subOptionsForOption(this.currentOption, lineText);
                    if (!options.isEmpty() && !event.getCode().isModifierKey())
                    {
                        this.dialog.updateOptions(options);
                        updateDialogPosition();
                    }
                    else
                    {
                        this.dialog.hide();
                        this.currentOption = null;
                    }
                    break;
            }
        });
    }

    private void updateDialogPosition()
    {
        this.editor.getCaretBounds().ifPresent(bounds -> this.dialog.show(bounds.getMinX(), bounds.getMinY()));
    }
}
