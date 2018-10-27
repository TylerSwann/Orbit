/*
 * <Orbit Editor. An Open Source Text Editor>
 *
 * Copyright (C) 2018 Jordan Swann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
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
