package io.orbit.api.text;

import io.orbit.api.SVGIcon;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Created by Tyler Swann on Saturday June 30, 2018 at 15:31
 */
public class FileType implements Serializable, Cloneable
{
    private String extension;
    private String mimeType;
    private String displayText;
    private SVGIcon icon;
    private Color themeColor;
    private Consumer<File> onCreate;

    /**
     *
     * @param extension - The file type extension without the dot '.'. ex: "java", "html"
     * @param mimeType - The mime type of the file. ex: "text/html", "application/javascript"
     * @param displayText - The text that will be displayed with your file type on the UI. ex: "HTML File", "Stylesheet"
     * @param icon - An icon representing your file type.
     * @param onCreate - (File -> void) Called after the user has created this type of file.
     *                   onCreate is called passing the requested file path including name. Once the function
     *                   is called, provide any template code you may want to include.
     */
    public FileType(String extension, String mimeType, String displayText, SVGIcon icon, Color themeColor, Consumer<File> onCreate)
    {
        this.extension = extension;
        this.mimeType = mimeType;
        this.displayText = displayText;
        this.icon = icon;
        this.themeColor = themeColor;
        this.onCreate = onCreate;
    }

    public String getExtension() { return extension; }
    public void setExtension(String extension) { this.extension = extension; }
    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    public SVGIcon getIcon() { return icon; }
    public void setIcon(SVGIcon icon) { this.icon = icon; }
    public String getDisplayText() { return this.displayText; }
    public void setDisplayText(String text) { this.displayText = text; }
    public Color getThemeColor() { return this.themeColor; }

    public void setOnCreate(Consumer<File> action) { this.onCreate = onCreate; }
    public Consumer<File> getOnCreate() { return onCreate; }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
            return true;
        if (!(other instanceof FileType))
            return false;
        FileType type = (FileType) other;
        return type.getExtension().equals(this.extension) && type.getMimeType().equals(this.mimeType);
    }

    @Override
    public FileType clone()
    {
        return new FileType(this.extension, this.mimeType, this.displayText, this.icon.clone(), this.themeColor, this.onCreate);
    }
}
