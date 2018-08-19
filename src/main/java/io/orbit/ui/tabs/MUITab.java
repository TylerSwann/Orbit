package io.orbit.ui.tabs;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * Created by Tyler Swann on Sunday August 19, 2018 at 13:02
 */
public class MUITab extends JFXButton
{
    private static final String DEFAULT_STYLE_CLASS = "mui-tab";
    private Node content;
    public Node getContent() {  return content;  }
    public void setContent(Node content) {  this.content = content;  }


    public MUITab(String text, Node graphic)
    {
        this(text);
        this.setGraphic(graphic);
    }

    public MUITab(String text)
    {
        super(text);
        this.setButtonType(JFXButton.ButtonType.FLAT);
        this.setRipplerFill(Color.rgb(255, 255, 255, 0.3));
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
    }
}
