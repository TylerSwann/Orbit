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

import javafx.beans.value.ObservableValue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Tyler Swann on Thursday April 12, 2018 at 13:55
 */
public class ProjectFile extends File
{
    private ObservableValue<String> textProperty;

    public ProjectFile(File physicalFile, ObservableValue<String> textProperty)
    {
        this(physicalFile.getPath(), textProperty);
    }

    public ProjectFile(String path, ObservableValue<String> textProperty)
    {
        super(path);
        this.textProperty = textProperty;
        if (this.exists() && this.isDirectory())
            throw new RuntimeException("ProjectFile cannot be a Directory. ProjectFile is for text files only!");
    }

    public String extension()
    {
        return this.getName().split("\\.")[1];
    }

    public boolean save()
    {
        boolean success = true;
        try
        {
            byte[] data = this.textProperty.getValue().getBytes();
            Files.write(Paths.get(this.getPath()), data);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            success = false;
        }
        return success;
    }
}
