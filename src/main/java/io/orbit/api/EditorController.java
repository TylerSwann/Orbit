package io.orbit.api;

import io.orbit.api.text.CodeEditor;
import java.io.File;

/**
 * Created by Tyler Swann on Sunday April 01, 2018 at 14:30
 */
public interface EditorController
{
    void start(File file, CodeEditor editor);
}
