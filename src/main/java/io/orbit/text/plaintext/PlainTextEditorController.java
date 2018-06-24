package io.orbit.text.plaintext;

import io.orbit.api.EditorController;
import io.orbit.api.text.CodeEditor;
import io.orbit.api.autocompletion.AutoCompletionDialog;
import java.io.File;


/**
 * Created by Tyler Swann on Saturday June 02, 2018 at 15:26
 */
public class PlainTextEditorController implements EditorController
{
    private AutoCompletionDialog modal;

    @Override
    public void start(File file, CodeEditor editor)
    {
    }

}
