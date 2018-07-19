package io.orbit.api.notification.modal;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.AnchorPane;

import java.util.function.Consumer;

/**
 * Created by Tyler Swann on Thursday July 19, 2018 at 15:16
 */
public class MUIInputModal extends MUIModal
{
    private JFXTextField textField = new JFXTextField();

    private StringProperty promptText = new SimpleStringProperty();
    public StringProperty promptTextProperty() { return promptText; }
    public String getPromptText() { return promptText.get(); }
    public void setPromptText(String text) { promptText.setValue(text); }

    private Consumer<String> onTextChange = __ -> {};
    public void setOnTextChange(Consumer<String> action) { this.onTextChange = action; }
    public Consumer<String> getOnTextChange() { return onTextChange; }

    private StringProperty text = new SimpleStringProperty();
    public StringProperty textProperty() { return text; }
    public String getText() { return this.text.get(); }
    public void setText(String text) {  this.textField.setText(text);  }

    public MUIInputModal(String title, String message, MUIModalButton... buttons)
    {
        super(title, message, buttons);
        build();
    }

    private void build()
    {
        this.textField.getStyleClass().add("mui-text-field");
        this.textField.promptTextProperty().bind(this.promptText);
        this.textProperty().bind(this.textField.textProperty());
        this.textField.textProperty().addListener((observable, oldValue, newValue) -> this.onTextChange.accept(newValue));
        this.getRoot().setMinHeight(150.0);
        this.textField.setPrefWidth(500.0);
        AnchorPane.setLeftAnchor(this.textField, 0.0);
        AnchorPane.setRightAnchor(this.textField, 0.0);
        this.getLabelsContainer().getChildren().add(this.textField);
    }
}
