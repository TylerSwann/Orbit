package io.orbit.api.text;

import io.orbit.api.SVGIcon;

import java.io.Serializable;

/**
 * Created by Tyler Swann on Saturday June 30, 2018 at 15:31
 */
public class FileType implements Serializable
{
    private String extension;
    private String mimeType;
    private SVGIcon icon;
    
    public FileType(String extension, String mimeType, SVGIcon icon)
    {
        this.extension = extension;
        this.mimeType = mimeType;
        this.icon = icon;
    }

    public String getExtension() { return extension; }
    public void setExtension(String extension) { this.extension = extension; }
    public String getMimeType() { return mimeType; }

    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    public SVGIcon getIcon() { return icon; }
    public void setIcon(SVGIcon icon) { this.icon = icon; }

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
}
