package io.orbit.settings;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * Created by Tyler Swann on Wednesday May 23, 2018 at 16:43
 */
public class UserHotKeys
{
    static KeyCombination SAVE_ALL = new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN);
    static KeyCombination SAVE = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
    static KeyCombination FIND_REPLACE = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);
    static KeyCombination FIND = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);

    public static KeyCombination SAVE_ALL_COMBO() { return SAVE_ALL; }
    public static KeyCombination SAVE_COMBO() { return SAVE; }
    public static KeyCombination FIND_REPLACE_COMBO() { return FIND_REPLACE; }
    public static KeyCombination FIND_COMBO() { return FIND; }
}


