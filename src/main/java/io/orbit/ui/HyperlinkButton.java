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
package io.orbit.ui;

import com.jfoenix.controls.JFXButton;
import io.orbit.ui.contextmenu.MUIMenuItem;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.kordamp.ikonli.Ikon;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Tyler Swann on Saturday May 12, 2018 at 15:31
 */
public class HyperlinkButton extends MUIMenuItem implements Hyperlink
{
    private URI uri;


    public HyperlinkButton(String text, URI uri)
    {
        super(text);
        this.init(uri);
    }

    public HyperlinkButton(Ikon icon, String text, String url)
    {
        super(icon, text);
        try
        {
            this.init(new URI(url));
        }
        catch (URISyntaxException ex) { ex.printStackTrace(); }
    }

    public HyperlinkButton(Ikon icon, String text, URI uri)
    {
        super(icon, text);
        this.init(uri);
    }

    private void init(URI uri)
    {
        this.uri = uri;
        this.setCursor(Cursor.HAND);
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> this.trigger());
    }

    @Override
    public URI getURI() {  return uri;  }
    @Override
    public void setURI(URI url) {  this.uri = url;  }

}

