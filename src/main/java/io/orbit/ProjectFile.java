package io.orbit;

import javafx.beans.value.ObservableValue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Tyler Swann on Thursday April 12, 2018 at 13:55
 */
public class ProjectFile extends OrbitFile
{
    private boolean wasModified = false;

    public ProjectFile(File physicalFile)
    {
        super(physicalFile);
    }

    public boolean wasModified()
    {
        return wasModified;
    }

    @Override
    public void setTextProperty(ObservableValue<String> textProperty)
    {
        super.setTextProperty(textProperty);
        this.textProperty.addListener(event -> this.wasModified = true);
    }
}
