package io.orbit.settings;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

/**
 * Created by Tyler Swann on Sunday September 23, 2018 at 17:00
 */
public class Shortcut extends KeyCombination
{
    private KeyCode key;
    private Modifier[] modifiers;

    public Shortcut(KeyCode key, Modifier... modifiers)
    {
        this.key = key;
        this.modifiers = modifiers;
    }

    @Override
    public String getDisplayText() { return getCombination().getDisplayText(); }
    @Override
    public boolean match(KeyEvent event) { return getCombination().match(event); }

    public KeyCode getKey() { return key; }
    public void setKey(KeyCode key) { this.key = key; }
    public Modifier[] getModifiers() {  return modifiers;  }
    public void setModifiers(Modifier... modifiers) { this.modifiers = modifiers; }

    private KeyCodeCombination getCombination()
    {
        if (this.key != null && this.modifiers != null)
            return new KeyCodeCombination(this.key, this.modifiers);
        else if (this.key != null)
            return new KeyCodeCombination(this.key);
        else if (this.modifiers != null)
            return new KeyCodeCombination(KeyCode.JAPANESE_ROMAN, this.modifiers);
        return null;
    }

}
