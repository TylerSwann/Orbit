package io.orbit.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import io.orbit.App;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PopupControl;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by Tyler Swann on Sunday April 15, 2018 at 16:40
 */
public class MUIDialog extends PopupControl
{
    private String title;
    private String message;
    private JFXButton left;
    private JFXButton right;
    private JFXTextField inputField;
    private VBox root;
    private Consumer<Optional<String>> onPrimaryClick = input -> {};
    private Runnable onSecondaryClick = () -> {};


    public MUIDialog(String title, String leftButtonText, String rightButtonText, String message)
    {
        this(title, leftButtonText, rightButtonText, message, null, false);
    }

    public MUIDialog(String title, String leftButtonText, String rightButtonText, String message, boolean isDestructive)
    {
        this(title, leftButtonText, rightButtonText, message, null, isDestructive);
    }

    public MUIDialog(String title, String leftButtonText, String rightButtonText, String message, String inputPrompt)
    {
        this(title, leftButtonText, rightButtonText, message, inputPrompt, false);
    }
    private MUIDialog(String title, String leftButtonText, String rightButtonText, String message, String inputPrompt, boolean isDestructive)
    {
        this.title = title;
        this.left = new JFXButton(leftButtonText);
        this.right = new JFXButton(rightButtonText);
        this.message = message;
        this.inputField = inputPrompt == null ? null : new JFXTextField(inputPrompt);

        if (this.inputField != null)
            this.inputField.getStyleClass().add("mui-text-field");
        initialize(isDestructive);
    }

    public void show(Node owner)
    {
        double x = owner.getScene().getWindow().getX() + owner.getScene().getWindow().getWidth() / 2.0;
        double y = owner.getScene().getWindow().getY() + owner.getScene().getWindow().getHeight() / 2.0;
        this.root.setScaleX(0.0);
        this.root.setScaleY(0.0);
        this.setOnShown(event -> {
            this.setX(x - this.getWidth() / 2.0);
            this.setY(y - this.getHeight() / 2.0);
            this.playAnimation(true, null);
        });
        super.show(owner, x, y);
    }

    private void initialize(boolean isDestructive)
    {
        double width = 768.0;
        double height = this.inputField == null ? 170.0 : 200.0;

        this.root = new VBox();
        this.root.getStyleClass().add("mui-dialog");
        HBox controlsContainer = new HBox();
        Label titleLabel = new Label(this.title);
        Label messageLabel = new Label(this.message);
        titleLabel.setTextAlignment(TextAlignment.LEFT);
        titleLabel.setAlignment(Pos.CENTER_LEFT);
        titleLabel.getStyleClass().add("mui-dialog-title");
        titleLabel.setPrefWidth(width);
        messageLabel.setTextAlignment(TextAlignment.LEFT);
        messageLabel.setAlignment(Pos.CENTER_LEFT);
        messageLabel.getStyleClass().add("mui-dialog-message");
        messageLabel.setPrefWidth(width);

        this.left.getStyleClass().add("button-secondary");
        this.right.getStyleClass().add("button-primary");
        this.left.setPrefSize(88.0, 36.0);
        this.right.setPrefSize(88.0, 36.0);
        this.left.setButtonType(JFXButton.ButtonType.FLAT);
        this.right.setButtonType(JFXButton.ButtonType.RAISED);
        this.left.setOnAction(event -> {
            this.onSecondaryClick.run();
            this.hide();
        });
        this.right.setOnAction(event -> {
            if (this.inputField != null)
                this.onPrimaryClick.accept(Optional.of(this.inputField.getText()));
            else
                this.onPrimaryClick.accept(Optional.empty());
            this.hide();
        });

        root.setPadding(new Insets(0.0, 25.0, 0.0, 25.0));
        root.setSpacing(15.0);
        root.setAlignment(Pos.CENTER);
        controlsContainer.setAlignment(Pos.BOTTOM_RIGHT);
        controlsContainer.setSpacing(20.0);

        root.setPrefSize(width, height);
        this.setPrefSize(width, height);

        DropShadow shadow = new DropShadow();
        shadow.setSpread(0.0);
        shadow.setOffsetX(1.5);
        shadow.setOffsetY(2.0);
        shadow.setColor(Color.BLACK);
        root.setEffect(shadow);

        controlsContainer.getChildren().addAll(left, right);
        if (isDestructive)
        {
            HBox iconTitleBox = new HBox(20.0);
            FontIcon alertIcon = new FontIcon(FontAwesomeSolid.EXCLAMATION_CIRCLE);
            alertIcon.setIconSize(25);
            alertIcon.setFill(Color.RED);
            iconTitleBox.getChildren().addAll(alertIcon, titleLabel);
            root.getChildren().addAll(iconTitleBox, messageLabel);
            this.right.getStyleClass().add("destructive");
        }
        else
            root.getChildren().addAll(titleLabel, messageLabel);

        if (this.inputField != null)
            root.getChildren().addAll(inputField, controlsContainer);
        else
            root.getChildren().add(controlsContainer);
        App.appTheme.sync(this.getScene().getStylesheets());
        this.getScene().setRoot(root);
    }

    private void playAnimation(boolean in, Runnable completion)
    {
        ScaleTransition transition = new ScaleTransition(Duration.millis(200), this.root);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(100.0), this.root);
        double from = in ? 0.0 : 1.0;
        double to = in ? 1.0 : 0.0;
        fadeTransition.setFromValue(from);
        fadeTransition.setToValue(to);
        transition.setFromX(from);
        transition.setFromY(from);
        transition.setToX(to);
        transition.setToY(to);
        fadeTransition.setOnFinished(event -> {
            if (completion != null)
                completion.run();
        });
        transition.play();
        fadeTransition.play();
    }

    @Override
    public void hide()
    {
        this.playAnimation(false, super::hide);
    }

    public void setOnPrimaryClick(Consumer<Optional<String>> action) { this.onPrimaryClick = action; }
    public void setOnSecondaryClick(Runnable action) { this.onSecondaryClick = action; }
    public Consumer<Optional<String>> getOnPrimaryClick() { return onPrimaryClick; }
    public Runnable getOnSecondaryClick() { return onSecondaryClick; }
}
