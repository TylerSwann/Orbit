package io.orbit.settings;

import javafx.beans.value.ObservableValue;

import java.io.File;

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
