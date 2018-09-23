package io.orbit.settings;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * Created by Tyler Swann on Friday September 21, 2018 at 19:12
 */
public class HotKeys
{

    public static final HotKeys DEFAULT;

    static {
        DEFAULT = new HotKeys();
        DEFAULT.setCut(new KeyCodeCombination(KeyCode.X, KeyCombination.SHORTCUT_DOWN));
        DEFAULT.setCopy(new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN));
        DEFAULT.setPaste(new KeyCodeCombination(KeyCode.V, KeyCombination.SHORTCUT_DOWN));
        DEFAULT.setUndo(new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN));
        DEFAULT.setRedo(new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN));
        DEFAULT.setSave(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
        DEFAULT.setSaveAll(new KeyCodeCombination(KeyCode.A, KeyCombination.SHORTCUT_DOWN));
        DEFAULT.setFind(new KeyCodeCombination(KeyCode.F, KeyCombination.SHORTCUT_DOWN));
        DEFAULT.setFindReplace(new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN));
        DEFAULT.setFindInProject(new KeyCodeCombination(KeyCode.F, KeyCodeCombination.SHORTCUT_DOWN, KeyCodeCombination.SHIFT_DOWN));
        DEFAULT.setFindReplaceInProject(new KeyCodeCombination(KeyCode.R, KeyCodeCombination.SHORTCUT_DOWN, KeyCodeCombination.SHIFT_DOWN));
    }

    private KeyCombination cut;
    private KeyCombination copy;
    private KeyCombination paste;
    private KeyCombination undo;
    private KeyCombination redo;
    private KeyCombination save;
    private KeyCombination saveAll;
    private KeyCombination find;
    private KeyCombination findReplace;
    private KeyCombination findInProject;
    private KeyCombination findReplaceProject;

    public HotKeys() { }

    public KeyCombination getCut() { return cut; }
    public void setCut(KeyCombination cut) { this.cut = cut; }
    public KeyCombination getCopy() { return copy; }
    public void setCopy(KeyCombination copy) { this.copy = copy; }
    public KeyCombination getPaste() { return paste; }
    public void setPaste(KeyCombination paste) { this.paste = paste; }
    public KeyCombination getUndo() { return undo; }
    public void setUndo(KeyCombination undo) { this.undo = undo; }
    public KeyCombination getRedo() { return redo; }
    public void setRedo(KeyCombination redo) { this.redo = redo; }
    public KeyCombination getSave() { return save; }
    public void setSave(KeyCombination save) { this.save = save; }
    public KeyCombination getSaveAll() { return saveAll; }
    public void setSaveAll(KeyCombination saveAll) { this.saveAll = saveAll; }
    public KeyCombination getFindReplace() { return findReplace; }
    public void setFindReplace(KeyCombination find_replace) { this.findReplace = find_replace; }
    public KeyCombination getFind() { return find; }
    public void setFind(KeyCombination find) { this.find = find; }
    public KeyCombination getFindInProject() { return findInProject; }
    public void setFindInProject(KeyCombination find_in_project) { this.findInProject = find_in_project; }
    public KeyCombination getFindReplaceInProject() { return findReplaceProject; }
    public void setFindReplaceInProject(KeyCombination find_replace_project) { this.findReplaceProject = find_replace_project; }
}

