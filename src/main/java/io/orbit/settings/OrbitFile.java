package io.orbit.settings;

import javafx.beans.value.ObservableValue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Tyler Swann on Saturday April 28, 2018 at 14:18
 */
public class OrbitFile extends File
{
    protected ObservableValue<String> textProperty;
    private String extension;

    public OrbitFile(String path)
    {
        super(path);
        setExtension();
    }
    public OrbitFile(File physicalFile)
    {
        super(physicalFile.getPath());
        setExtension();
    }

    private void setExtension()
    {
        if (this.isDirectory())
            extension = "";
        else
        {
            int index = this.getName().lastIndexOf('.');
            if (index > 0)
                extension = this.getName().substring(index + 1);
        }
    }

    public void setTextProperty(ObservableValue<String> textProperty)
    {
        this.textProperty = textProperty;
    }

    public void save()
    {
        if (this.textProperty == null)
        {
            System.out.println("ERROR Text property in OrbitFile is null");
            return;
        }
        try
        {
            byte[] data = textProperty.getValue().getBytes(Charset.defaultCharset());
            Files.write(Paths.get(this.getPath()), data);
        }
        catch (IOException ex)
        {
            System.out.println(String.format("ERROR saving OrbitFile at path %s", this.getPath()));
            ex.printStackTrace();
        }
    }

    public String getExtension() {  return extension;  }
}
