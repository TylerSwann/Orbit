package io.orbit.settings;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * Created by Tyler Swann on Wednesday May 23, 2018 at 16:43
 */
public class UserHotKeys
{
    static KeyCombination CUT = new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN);
    static KeyCombination COPY = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
    static KeyCombination PASTE = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);
    static KeyCombination UNDO = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
    static KeyCombination REDO = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);
    static KeyCombination SAVE = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
    static KeyCombination SAVE_ALL = new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN);
    static KeyCombination FIND_REPLACE = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);
    static KeyCombination FIND = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);

    public static KeyCombination CUT() { return CUT; }
    public static KeyCombination COPY() { return COPY; }
    public static KeyCombination PASTE() { return PASTE; }
    public static KeyCombination UNDO() { return UNDO; }
    public static KeyCombination REDO() { return REDO; }
    public static KeyCombination SAVE() { return SAVE; }
    public static KeyCombination SAVE_ALL() { return SAVE_ALL; }
    public static KeyCombination FIND() { return FIND; }
    public static KeyCombination FIND_REPLACE() { return FIND_REPLACE; }
}
