package io.orbit.ui.contextmenu;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Skin;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * Created by Tyler Swann on Saturday July 07, 2018 at 14:42
 */
public class MUIContextMenu extends PopupControl
{
    private static final String DEFAULT_STYLE_CLASS = "mui-context-menu";
    private final ObservableList<MUIMenuItem> items = FXCollections.observableArrayList();
    protected final VBox root = new VBox();
    private Node owner;

    public MUIContextMenu()
    {
        this(null, Collections.emptyList());
    }

    public MUIContextMenu(Node owner)
    {
        this(owner, Collections.emptyList());
    }

    public MUIContextMenu(Node owner, Collection<? extends MUIMenuItem> items)
    {
        this.items.addAll(items);
        this.owner = owner;
        this.root.getStyleClass().add(DEFAULT_STYLE_CLASS);
        registerListeners();
    }

    private void registerListeners()
    {
        this.setAutoHide(true);
        if (this.owner != null)
            this.owner.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> this.show(event.getScreenX(), event.getScreenY()));
        this.items.addListener((ListChangeListener<MUIMenuItem>) change -> {
            while (change.next())
            {
                if (change.wasRemoved())
                {
                    change.getRemoved().forEach(removed -> {
                        if (removed.prefWidthProperty().isBound())
                            removed.prefWidthProperty().unbind();
                        this.root.getChildren().removeAll(removed);
                    });
                }
                if (change.wasAdded())
                {
                    change.getAddedSubList().forEach(added -> {
                        if (!added.prefWidthProperty().isBound())
                            added.prefWidthProperty().bind(this.widthProperty());
                        if (added instanceof MUIMenu)
                            ((MUIMenu) added).setParent(this);
                        this.root.getChildren().add(added);
                    });
                }
            }
        });
        this.addEventHandler(WindowEvent.WINDOW_HIDING, __ -> {
            this.hideTransition(() -> {});
        });
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (!(event.getTarget() instanceof MUIMenu))
                Platform.runLater(this::hide);
        });
        this.items.forEach(item -> {
            item.prefWidthProperty().bind(this.widthProperty());
            if (item instanceof MUIMenu)
                ((MUIMenu) item).setParent(this);
        });

        this.root.setPrefSize(130.0, 100.0);
        this.root.getChildren().addAll(this.items);
        this.getScene().setRoot(root);
    }

    public void show(Node owner, double x, double y)
    {
        this.owner = owner;
        this.show(x, y);
    }

    public void show(double x, double y)
    {
        this.setX(x);
        this.setY(y);
        this.owner = Objects.requireNonNull(owner);
        this.show(this.owner.getScene().getWindow());
        this.showTransition();
    }

    @Override
    public void show(Window owner, double x, double y)
    {
        super.show(owner, x, y);
        showTransition();
    }

    @Override
    public void show(Window ownerWindow)
    {
        super.show(ownerWindow);
        showTransition();
    }

    protected void showTransition()
    {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(50), this.root);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(25), this.root);
        scaleTransition.setFromY(0.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();
        fadeTransition.play();
    }

    protected void hideTransition(Runnable completion)
    {
        completion.run();
    }

    @Override
    protected Skin<?> createDefaultSkin()
    {
        return new MUIContextMenuSkin(this);
    }

    public Node getOwner() { return owner; }
    public void setOwner(Node owner) {  this.owner = owner;  }
    public VBox getRoot() { return root; }
    public ObservableList<MUIMenuItem> itemsProperty() {  return this.items;  }
    public List<MUIMenuItem> getItems() {  return this.items;  }
}