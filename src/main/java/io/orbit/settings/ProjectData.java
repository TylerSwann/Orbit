package io.orbit.settings;

import javafx.beans.property.SimpleObjectProperty;
import java.io.File;
import java.util.List;

/**
 * Created by Tyler Swann on Thursday March 29, 2018 at 15:04
 */
public class ProjectData
{
    private SimpleObjectProperty<List<File>> openEditors = new SimpleObjectProperty<>();
    private SimpleObjectProperty<File> openFile = new SimpleObjectProperty<>();
    private SimpleObjectProperty<File> projectRoot = new SimpleObjectProperty<>();

    public ProjectData() { }

    public File getProjectRoot() { return projectRoot.get(); }
    public void setProjectRoot(File projectRoot) { this.projectRoot.setValue(projectRoot); }

    public List<File> getOpenEditors() {  return openEditors.get();  }
    public void setOpenEditors(List<File> openEditors) { this.openEditors.setValue(openEditors); }

    public File getOpenFile() { return this.openFile.get(); }
    public void setOpenFile(File openFile) { this.openFile.setValue(openFile); }
}
