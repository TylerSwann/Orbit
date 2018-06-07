package io.orbit.webtools.test;

import io.orbit.api.EditorController;
import io.orbit.api.event.AutoCompletionModalEvent;
import io.orbit.api.text.CodeEditor;
import io.orbit.ui.AutoCompletionModal;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.wellbehaved.event.EventPattern;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;

import java.io.File;
import java.util.function.BooleanSupplier;

/**
 * Created by Tyler Swann on Saturday June 02, 2018 at 15:26
 */
public class PlainTextEditorController implements EditorController, BooleanSupplier
{
    private AutoCompletionModal modal;

    @Override
    public void start(File file, CodeEditor editor)
    {
        this.modal = new AutoCompletionModal(editor);

        editor.requestFollowCaret();
        InputMap<Event> disabled = InputMap.consume(EventPattern.anyOf(
                EventPattern.keyPressed(KeyCode.UP),
                EventPattern.keyPressed(KeyCode.DOWN)
        ));
        Nodes.addInputMap(editor, InputMap.when(this, disabled));
        modal.addRandomOptions("Option: ");
        modal.addEventHandler(AutoCompletionModalEvent.OPTION_WAS_SELECTED, event -> {
            event.selectedOption.ifPresent(option -> System.out.println(option.getText()));
        });
        editor.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> modal.hide());
        editor.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            switch (event.getCode())
            {
                case PERIOD:
                case ENTER:
                case COLON:
                case BACK_SPACE:
                case SPACE:
                    if (modal.isShowing())
                        modal.hide();
                    break;
                case UP:
                case DOWN:
                    break;
                default:
                     editor.getCaretBounds().ifPresent(bounds -> modal.show(bounds.getMinX(), bounds.getMinY()));
            }
        });

    }

    @Override
    public boolean getAsBoolean()
    {
        return this.modal.isShowing();
    }
}
