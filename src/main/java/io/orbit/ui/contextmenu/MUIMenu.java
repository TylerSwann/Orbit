package io.orbit.ui.contextmenu;

import io.orbit.App;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.control.ContentDisplay;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tyler Swann on Saturday July 07, 2018 at 15:55
 */
public class MUIMenu extends MUIMenuItem
{
    private MUISubContextMenu submenu;
    private MUIContextMenu parentMenu;
    private boolean mouseIsHovering = false;

    public MUIMenu(String text, MUIMenuItem... items)
    {
        super(FontAwesomeSolid.ARROW_RIGHT, text);
        this.setContentDisplay(ContentDisplay.RIGHT);
        this.submenu = new MUISubContextMenu();
        this.submenu.getItems().addAll(Arrays.asList(items));
        registerListeners();
    }

    protected void setParent(MUIContextMenu parentMenu)
    {
        this.parentMenu = parentMenu;
    }

    private void registerListeners()
    {
        this.submenu.root.addEventFilter(MouseEvent.MOUSE_ENTERED, __ -> Platform.runLater(() -> this.mouseIsHovering = true));
        this.submenu.root.addEventFilter(MouseEvent.MOUSE_EXITED, __ -> Platform.runLater(() -> {
            this.mouseIsHovering = false;
            PauseTransition pause = new PauseTransition(Duration.millis(300));
            pause.setOnFinished(___ -> {
                if (!mouseIsHovering && this.submenu.isShowing())
                    this.submenu.hide();
            });
            pause.play();
        }));
        this.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            mouseIsHovering = true;
            if (!this.submenu.isShowing())
            {
                Bounds boundsInScreen = this.parentMenu.getOwner().localToScreen(this.parentMenu.getOwner().getBoundsInLocal());
                double x = boundsInScreen.getMinX() + this.parentMenu.getX() - this.parentMenu.getWidth();
                double y = boundsInScreen.getMinY() + this.parentMenu.getY() - (this.parentMenu.getHeight() / 2.0 + 5.0);
                this.submenu.show(this.parentMenu, x, y);
            }
        });
        this.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            mouseIsHovering = false;
            PauseTransition pause = new PauseTransition(Duration.millis(300));
            pause.setOnFinished(__ -> {
                if (!mouseIsHovering && this.submenu.isShowing())
                    this.submenu.hide();
            });
            pause.play();
        });

    }

    public List<MUIMenuItem> getItems() {  return this.submenu.getItems();  }

    private class MUISubContextMenu extends MUIContextMenu
    {
        MUISubContextMenu()
        {
            super();
            this.setAutoHide(false);
        }

        @Override
        protected void showTransition()
        {
            Duration duration = Duration.millis(100.0);
            TranslateTransition translate = new TranslateTransition(duration, this.root);
            ScaleTransition scale = new ScaleTransition(duration, this.root);
            FadeTransition fade = new FadeTransition(Duration.millis(200.0), this.root);
            double width = this.root.getWidth();
            double x = this.root.getTranslateX() - (width / 2.0);
            translate.setFromX(x);
            translate.setToX(this.root.getTranslateX());
            scale.setFromX(0.0);
            scale.setToX(1.0);
            fade.setFromValue(0.0);
            fade.setToValue(1.0);
            translate.play();
            scale.play();
            fade.play();
        }

        @Override
        protected void hideTransition(Runnable completion)
        {
            Duration duration = Duration.millis(100.0);
            TranslateTransition translate = new TranslateTransition(duration, this.root);
            ScaleTransition scale = new ScaleTransition(duration, this.root);
            FadeTransition fade = new FadeTransition(Duration.millis(200.0), this.root);
            double width = this.root.getWidth();
            double x = this.root.getTranslateX() - (width / 2.0);
            translate.setToX(x);
            translate.setFromX(this.root.getTranslateX());
            scale.setFromX(1.0);
            scale.setToX(0.0);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            translate.play();
            scale.play();
            fade.play();
            fade.setOnFinished(__ -> completion.run());
        }
    }
}
