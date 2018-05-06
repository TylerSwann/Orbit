package io.orbit;

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

    public OrbitFile(String path)
    {
        super(path);
    }
    public OrbitFile(File physicalFile)
    {
        super(physicalFile.getPath());
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
}
