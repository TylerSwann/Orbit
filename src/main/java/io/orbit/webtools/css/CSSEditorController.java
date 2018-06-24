package io.orbit.webtools.css;

import io.orbit.api.EditorController;
import io.orbit.api.text.CodeEditor;
import io.orbit.api.autocompletion.AutoCompletionDialog;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
            System.out.println(option.insertedText);
            int lineNumber = this.editor.getFocusPosition().line;
            int characterNumber = this.editor.getFocusPosition().caretPositionInDocument;
            if (option.insertedTextFragment != null)
                this.editor.insertText(characterNumber, option.insertedTextFragment);
            else
                System.out.println("CSS3AutoCompletionOption has null inserted text fragment");
            if (option.subOptions.size() > 0)
            {
                this.currentOption = option;
                this.dialog.updateOptions(option.subOptions);
                updateDialogPosition();
            }
            else
                this.currentOption = null;
        }));
        this.editor.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
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
                    {
                        options = this.provider.subOptionsForOption(this.currentOption, lineText);
                    }
                    if (!options.isEmpty() && !event.getCode().isModifierKey())
                    {
                        this.dialog.updateOptions(options);
                        updateDialogPosition();
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
