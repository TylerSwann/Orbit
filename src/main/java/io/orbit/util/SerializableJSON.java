package io.orbit.util;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by Tyler Swann on Thursday March 29, 2018 at 14:48
 */
public class SerializableJSON
{
    private static final Gson gson = new Gson();

    public static <T> T loadFromFile(Class<? extends T> classType, File file)
    {
        try
        {
            JsonReader reader = new JsonReader(new FileReader(file));
            return gson.fromJson(reader, classType);
        }
        catch (Exception ex)
        {
            System.out.println(String.format("Couldn't load SerializableJSON object from file at path %s", file.getPath()));
            ex.printStackTrace();
        }
        throw new RuntimeException(String.format("Couldn't load SerializableJSON object from file at path %s", file.getPath()));
    }

    public void writeToFile(File file)
    {
        try
        {
            String json = gson.toJson(this);
            Files.write(Paths.get(file.getPath()), json.getBytes());
        }
        catch (Exception ex)
        {
            System.out.println(String.format("Couldn't write SerializableJSON object to file at path %s", file.getPath()));
            ex.printStackTrace();
        }
    }
}