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
        DEFAULT.setCut(new Shortcut(KeyCode.X, KeyCombination.SHORTCUT_DOWN));
        DEFAULT.setCopy(new Shortcut(KeyCode.C, KeyCombination.SHORTCUT_DOWN));
        DEFAULT.setPaste(new Shortcut(KeyCode.V, KeyCombination.SHORTCUT_DOWN));
        DEFAULT.setUndo(new Shortcut(KeyCode.Z, KeyCombination.SHORTCUT_DOWN));
        DEFAULT.setRedo(new Shortcut(KeyCode.Z, KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN));
        DEFAULT.setSave(new Shortcut(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
        DEFAULT.setSaveAll(new Shortcut(KeyCode.A, KeyCombination.SHORTCUT_DOWN));
        DEFAULT.setFind(new Shortcut(KeyCode.F, KeyCombination.SHORTCUT_DOWN));
        DEFAULT.setFindReplace(new Shortcut(KeyCode.R, KeyCombination.SHORTCUT_DOWN));
        DEFAULT.setFindInProject(new Shortcut(KeyCode.F, KeyCodeCombination.SHORTCUT_DOWN, KeyCodeCombination.SHIFT_DOWN));
        DEFAULT.setFindReplaceInProject(new Shortcut(KeyCode.R, KeyCodeCombination.SHORTCUT_DOWN, KeyCodeCombination.SHIFT_DOWN));
    }

    private Shortcut cut;
    private Shortcut copy;
    private Shortcut paste;
    private Shortcut undo;
    private Shortcut redo;
    private Shortcut save;
    private Shortcut saveAll;
    private Shortcut find;
    private Shortcut findReplace;
    private Shortcut findInProject;
    private Shortcut findReplaceProject;

    public HotKeys() { }

    public Shortcut getCut() { return cut; }
    public void setCut(Shortcut cut) { this.cut = cut; }
    public Shortcut getCopy() { return copy; }
    public void setCopy(Shortcut copy) { this.copy = copy; }
    public Shortcut getPaste() { return paste; }
    public void setPaste(Shortcut paste) { this.paste = paste; }
    public Shortcut getUndo() { return undo; }
    public void setUndo(Shortcut undo) { this.undo = undo; }
    public Shortcut getRedo() { return redo; }
    public void setRedo(Shortcut redo) { this.redo = redo; }
    public Shortcut getSave() { return save; }
    public void setSave(Shortcut save) { this.save = save; }
    public Shortcut getSaveAll() { return saveAll; }
    public void setSaveAll(Shortcut saveAll) { this.saveAll = saveAll; }
    public Shortcut getFindReplace() { return findReplace; }
    public void setFindReplace(Shortcut find_replace) { this.findReplace = find_replace; }
    public Shortcut getFind() { return find; }
    public void setFind(Shortcut find) { this.find = find; }
    public Shortcut getFindInProject() { return findInProject; }
    public void setFindInProject(Shortcut find_in_project) { this.findInProject = find_in_project; }
    public Shortcut getFindReplaceInProject() { return findReplaceProject; }
    public void setFindReplaceInProject(Shortcut find_replace_project) { this.findReplaceProject = find_replace_project; }
}

