package io.orbit.api;

import io.orbit.api.text.CodeEditor;
import java.io.File;

/**
 * Created by Tyler Swann on Sunday April 01, 2018 at 14:30
 */
public interface EditorController
{
    /**
     *
     * Called when a file is loaded
     * @param file - The source file being opened in the editor
     * @param editor - The editor that will be controlled by this EditorController
     */
    void start(File file, CodeEditor editor);

    /**************************************************************************
     *                                                                        *
     *                              Button Events                             *
     *                                                                        *
     **************************************************************************/

    /**
     * Called when the play button, inside the Menu Bar, is clicked
     */
    default void playWasClicked() { }
    /**
     * Called when the stop button, inside the Menu Bar, is clicked
     */
    default void stopWasClicked() { }


    /**************************************************************************
     *                                                                        *
     *                             Document events                            *
     *                                                                        *
     **************************************************************************/
    default void documentWillClosed(File sourceFile) {  }
}
