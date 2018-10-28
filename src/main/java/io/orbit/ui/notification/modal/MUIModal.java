package io.orbit.ui.notification.modal;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Skin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Arrays;

/**
 * Created by Tyler Swann on Sunday July 15, 2018 at 18:54
 */
public class MUIModal extends PopupControl
{
    private static final String DEFAULT_STYLE_CLASS = "mui-modal";
    private static final String MESSAGE_STYLE_CLASS = "message";
    private static final String TITLE_STYLE_CLASS = "title";

    private Label titleLabel;
    private Label messageLabel;
    private AnchorPane root;
    private VBox labelsContainer;
    private HBox buttonsContainer;
    private HBox headerContainer;
    private FontIcon icon;
    private MUIModalButton[] buttons;

    public MUIModal(String title, String message, Ikon icon, MUIModalButton... buttons)
    {
        this(title, message, buttons);
        this.icon.setIconCode(icon);
        this.headerContainer.getChildren().add(0, this.icon);
        this.icon.setIconSize(25);
    }

    public MUIModal(String title, String message, MUIModalButton... buttons)
    {
        this.titleLabel = new Label(title);
        this.messageLabel = new Label(message);
        this.root = new AnchorPane();
        this.icon = new FontIcon();
        this.root.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.titleLabel.getStyleClass().add(TITLE_STYLE_CLASS);
        this.messageLabel.getStyleClass().add(MESSAGE_STYLE_CLASS);
        this.buttons = buttons;
        this.build();
        this.registerListeners();
    }
    private void build()
    {
        this.root.setMinSize(600.0, 100.0);
        this.labelsContainer = new VBox();
        this.labelsContainer.setAlignment(Pos.BOTTOM_LEFT);
        this.labelsContainer.setPadding(new Insets(20.0, 20.0, 30.0, 30.0));
        this.labelsContainer.setSpacing(15.0);

        this.buttonsContainer = new HBox();
        this.buttonsContainer.setAlignment(Pos.BOTTOM_RIGHT);
        this.buttonsContainer.setPadding(new Insets(0.0, 20.0, 10.0, 0.0));
        this.buttonsContainer.setSpacing(10.0);

        this.headerContainer = new HBox();
        this.headerContainer.setAlignment(Pos.CENTER_LEFT);
        this.headerContainer.setSpacing(20.0);
        this.headerContainer.getChildren().add(this.titleLabel);

        this.titleLabel.setWrapText(true);
        this.messageLabel.setWrapText(true);

        Arrays.asList(this.buttons).forEach(button -> button.setDisableVisualFocus(true));
        AnchorPane.setTopAnchor(this.labelsContainer, 0.0);
        AnchorPane.setLeftAnchor(this.labelsContainer, 0.0);
        AnchorPane.setRightAnchor(this.labelsContainer, 0.0);
        AnchorPane.setBottomAnchor(this.buttonsContainer, 0.0);
        AnchorPane.setLeftAnchor(this.buttonsContainer, 0.0);
        AnchorPane.setRightAnchor(this.buttonsContainer, 0.0);

        Arrays.asList(this.buttons).forEach(button -> button.addEventHandler(MouseEvent.MOUSE_CLICKED, __ -> this.hide()));

        this.labelsContainer.getChildren().addAll(this.headerContainer, this.messageLabel);
        this.buttonsContainer.getChildren().addAll(Arrays.asList(this.buttons));
        this.root.getChildren().addAll(this.labelsContainer, this.buttonsContainer);
        this.getScene().setRoot(this.root);
//        this.labelsContainer.setStyle("-fx-background-color: red;");
//        this.buttonsContainer.setStyle("-fx-background-color: blue;");
//        this.headerContainer.setStyle("-fx-background-color: green;");
//        this.icon.setStyle("-fx-background-color: yellow;");
    }

    @Override
    public void show(Window owner)
    {
        super.show(owner);
        FadeTransition fade = new FadeTransition(Duration.millis(150.0), this.root);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }

    @Override
    public void hide()
    {
        FadeTransition fade = new FadeTransition(Duration.millis(150.0), this.root);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.play();
        fade.setOnFinished(__-> super.hide());
    }

    private void registerListeners()
    {
        this.addEventHandler(WindowEvent.WINDOW_SHOWING, __ -> Platform.runLater(() -> {
            double height = this.getHeight();
            AnchorPane.setBottomAnchor(this.labelsContainer, height * 0.15);
            AnchorPane.setTopAnchor(this.buttonsContainer, height * 0.60);
        }));
    }

    @Override
    protected Skin<?> createDefaultSkin()
    {
        return new MUIModalSkin(this);
    }

    protected AnchorPane getRoot() {  return this.root;  }
    protected VBox getLabelsContainer() { return this.labelsContainer; }
    protected HBox getButtonsContainer() { return this.buttonsContainer; }

    public FontIcon getIcon() { return icon; }
    public void setIcon(Ikon icon) {  this.icon.setIconCode(icon);  }
}
