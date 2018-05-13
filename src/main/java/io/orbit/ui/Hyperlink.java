package io.orbit.ui;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

/**
 * Created by Tyler Swann on Saturday May 12, 2018 at 15:30
 */
public interface Hyperlink
{
    URI getURI();
    void setURI(URI uri);
    default void trigger()
    {
        Desktop desktop = Desktop.getDesktop();
        if (desktop == null || !desktop.isSupported(Desktop.Action.BROWSE))
        {
            System.out.println("ERROR: java.awt.Desktop is not available on this system");
            return;
        }
        try
        {
            desktop.browse(this.getURI());
        }
        catch (IOException ex)
        {
            System.out.println("ERROR triggering hyperlink!");
            ex.printStackTrace();
        }
    }
}
