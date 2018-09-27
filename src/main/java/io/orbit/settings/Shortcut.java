package io.orbit.settings;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * Created by Tyler Swann on Sunday September 23, 2018 at 17:00
 */
public class Shortcut extends KeyCombination
{
    private KeyCode key;
    private Modifier[] modifiers;

    public Shortcut()
    {
        this.key = KeyCode.ENTER;
        this.modifiers = new Modifier[]{};
    }

    public Shortcut(KeyCode key, Modifier... modifiers)
    {
        this.key = key;
        this.modifiers = modifiers;
    }

    public KeyCode getKey() { return key; }
    public Modifier[] getModifiers() {  return modifiers;  }
    public void setKey(KeyCode key) { this.key = key; }
    public void setModifiers(Modifier... modifiers) {  this.modifiers = modifiers;  }

    @Override
    public String getDisplayText()
    {
        KeyCodeCombination combination = new KeyCodeCombination(this.key, this.modifiers);
        return combination.getDisplayText();
    }
}
