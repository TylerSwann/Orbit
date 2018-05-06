package io.orbit.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Tyler Swann on Friday March 30, 2018 at 19:33
 */
public class JSON
{
    private static final Gson gson = new GsonBuilder().create();

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

    public static <T> void writeToFile(T object, File file)
    {
        try
        {
            String json = gson.toJson(object);
            Files.write(Paths.get(file.getPath()), json.getBytes());
        }
        catch (IOException ex)
        {
            System.out.println(String.format("Couldn't write JSON object to file at path: %s", file.getPath()));
            ex.printStackTrace();
        }
    }
}
