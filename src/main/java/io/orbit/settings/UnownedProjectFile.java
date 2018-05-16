package io.orbit.settings;

import java.io.File;


/**
 * Created by Tyler Swann on Saturday April 28, 2018 at 14:20
 */
public class UnownedProjectFile extends OrbitFile
{
    public enum UnownedProjectFileMode {  EDIT_SYNTAX_THEME, EDIT_UI_THEME  }

    public final UnownedProjectFileMode mode;

    public UnownedProjectFile(String path, UnownedProjectFileMode mode)
    {
        super(path);
        this.mode = mode;
    }

    public UnownedProjectFile(File physicalFile, UnownedProjectFileMode mode)
    {
        super(physicalFile);
        this.mode = mode;
    }
}
