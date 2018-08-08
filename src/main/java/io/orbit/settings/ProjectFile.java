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
