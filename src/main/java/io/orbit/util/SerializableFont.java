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
package io.orbit.util;

import io.orbit.App;
import javafx.scene.text.Font;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Tyler Swann on Friday March 30, 2018 at 20:20
 */
public class SerializableFont
{
    private String family;
    private Double size;
    private transient Font font;

    public SerializableFont() { }

    public SerializableFont(String fontFamily, double fontSize)
    {
        this.family = fontFamily;
        this.size = fontSize;
        this.font = new Font(fontFamily, fontSize);
    }

    public String getFamily() { return family; }
    public void setFamily(String fontFamily)
    {
        double fontSize = 12.0;
        if (this.size != null && this.font == null)
            fontSize = this.size;
        this.font = new Font(fontFamily, fontSize);
        this.family = fontFamily;
    }

    public double getSize() { return size; }
    public void setSize(double fontSize)
    {
        if (this.font == null && this.family != null)
            this.font = new Font(this.family, size);
        this.size = fontSize;
    }
    public Font getFont() { return font; }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
            return true;
        if (!(other instanceof SerializableFont))
            return false;
        SerializableFont otherFont = (SerializableFont) other;
        return otherFont.family.equals(this.family) && otherFont.size.equals(this.size);
    }

    public static SerializableFont fromFile(File file)
    {
        URL url;
        try { url = file.toURI().toURL(); }
        catch (MalformedURLException ex)
        {
            System.out.println(String.format("ERROR loading font from file at path %s", file.getPath()));
            ex.printStackTrace();
            return null;
        }
        Font font = Font.loadFont(url.toExternalForm(), 12.0);
        assert font != null;
        return fromFont(font);
    }

    public static SerializableFont fromResources(String url)
    {
        URL fontUrl = App.class.getClassLoader().getResource(url);
        if (fontUrl == null)
            return null;
        Font font = Font.loadFont(fontUrl.toExternalForm(), 12.0);
        assert font != null;
        return fromFont(font);
    }

    public static SerializableFont fromFont(Font font)
    {
        SerializableFont serializableFont = new SerializableFont(font.getFamily(), font.getSize());
        serializableFont.font = font;
        return serializableFont;
    }

    @Override
    public String toString()
    {
        return String.format("SerializableFont: \n\tFamily: %s\n\tSize: %f", this.family, this.size);
    }
}
