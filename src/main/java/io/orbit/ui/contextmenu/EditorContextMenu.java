package io.orbit.ui.contextmenu;

import com.jfoenix.controls.JFXButton;
import io.orbit.api.text.CodeEditor;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Skin;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tyler Swann on Friday July 06, 2018 at 18:55
 */
public class EditorContextMenu extends PopupControl
{
    private VBox root = new VBox();
    private CodeEditor editor;

    public EditorContextMenu(CodeEditor editor)
    {
        this.root.getStyleClass().add("editor-context-menu");
        this.editor = editor;
        build();
        this.registerListeners();
    }

    private void registerListeners()
    {
        this.editor.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> {
            this.setX(event.getScreenX());
            this.setY(event.getScreenY());
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(50), this.root);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(25), this.root);
            scaleTransition.setFromY(0.0);
            scaleTransition.setToY(1.0);
            this.show(this.editor.getScene().getWindow());
            scaleTransition.play();
            fadeTransition.play();
        });
    }

    @Override
    protected Skin<?> createDefaultSkin()
    {
        return null;
    }

    private void build()
    {
        this.setAutoHide(true);
        JFXButton undo = new JFXButton(" Undo");
        JFXButton redo = new JFXButton(" Redo");
        JFXButton cut = new JFXButton(" Cut");
        JFXButton copy = new JFXButton(" Copy");
        JFXButton paste = new JFXButton(" Paste");
        JFXButton selectAll = new JFXButton(" Select All");
        undo.setGraphic(new FontIcon(FontAwesomeSolid.UNDO));
        redo.setGraphic(new FontIcon(FontAwesomeSolid.REDO));
        cut.setGraphic(new FontIcon(FontAwesomeSolid.CUT));
        copy.setGraphic(new FontIcon(FontAwesomeSolid.COPY));
        paste.setGraphic(new FontIcon(FontAwesomeSolid.PASTE));
        selectAll.setGraphic(new FontIcon(FontAwesomeSolid.FILE_ALT));
        List<JFXButton> items = Arrays.asList(
                undo,
                redo,
                cut,
                copy,
                paste,
                selectAll
        );
        this.root.getChildren().addAll(items);
        items.forEach(item -> {
            item.prefWidthProperty().bind(this.widthProperty());
            item.getStyleClass().add("mui-context-item");
        });
        this.root.setPrefSize(130.0, 200.0);

        this.getScene().setRoot(root);
    }
}
