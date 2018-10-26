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
 */
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
