package io.orbit.ui;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import java.net.URI;

/**
 * Created by Tyler Swann on Saturday May 12, 2018 at 15:31
 */
public class HyperlinkButton extends JFXButton implements Hyperlink
{
    private URI uri;

    private HyperlinkButton() {  }

    public HyperlinkButton(String text, URI uri)
    {
        super(text);
        this.init(uri);
    }

    public HyperlinkButton(String text, Node graphic, URI uri)
    {
        super(text, graphic);
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

