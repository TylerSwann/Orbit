package io.orbit.ui;

import com.jfoenix.controls.JFXListView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PopupControl;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Swann on Wednesday May 30, 2018 at 14:38
 */
public class AutoCompletionModal extends PopupControl
{
    private JFXListView<Label> optionsView;
    private Node owner;
    private final double xOffset = 10.0;
    private final double yOffset = 0.0;
    private int selectedIndex = 0;

    private ObjectProperty<List<String>> options = new SimpleObjectProperty<>(new ArrayList<>());
    public ObjectProperty<List<String>> optionsProperty() { return options; }
    public List<String> getOptions() { return options.get(); }
    public void setOptions(List<String> options) { this.options.setValue(options); }

    public AutoCompletionModal(List<String> options, Node owner)
    {
        this(owner);
        this.setOptions(options);
    }
    public AutoCompletionModal(Node owner)
    {
        this.owner = owner;
        this.optionsView = new JFXListView<>();
        this.setAutoHide(true);
        double width = 600.0;
        double height = 250.0;
        this.setPrefSize(width, height);
        this.optionsView.setPrefSize(width, height);
        AnchorPane.setTopAnchor(optionsView, 0.0);
        AnchorPane.setBottomAnchor(optionsView, 0.0);
        AnchorPane.setLeftAnchor(optionsView, 0.0);
        AnchorPane.setRightAnchor(optionsView, 630.0);
        updateItems();
        this.getScene().setRoot(this.optionsView);
        registerListeners();
        this.setStyle("-fx-background-color: transparent;");
        this.optionsView.setStyle("-fx-background-radius: 5px; " +
                                  "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.5), 10.0, 0.0, 1.5, 2.5);" +
                                  "-fx-background-color: white;");
    }


    private void registerListeners()
    {
        this.optionsProperty().addListener(change -> this.updateItems());
        this.owner.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (!(event.getCode().isArrowKey()))
                return;
            switch (event.getCode())
            {
                case UP:
                    if (selectedIndex - 1 < 0)
                        selectedIndex = this.options.get().size() - 1;
                    else
                        selectedIndex--;
                    this.optionsView.getSelectionModel().select(selectedIndex);
                    if (selectedIndex % 7 == 0)
                        this.optionsView.scrollTo(selectedIndex);
                    break;
                case DOWN:
                    if (selectedIndex + 1 > this.options.get().size() - 1)
                        selectedIndex = 0;
                    else
                        selectedIndex++;
                    this.optionsView.getSelectionModel().select(selectedIndex);
                    if (selectedIndex % 7 == 0)
                        this.optionsView.scrollTo(selectedIndex);
                    break;
                default: break;
            }
        });
    }

    public void show(double x, double y)
    {
        super.show(this.owner.getScene().getWindow());
        this.optionsView.getSelectionModel().selectFirst();
        this.setX(x + this.xOffset);
        this.setY(y + this.yOffset);
    }

    @Override
    public void hide()
    {
        super.hide();
    }

    private void updateItems()
    {
        if (this.optionsView.getItems().size() > 0)
            this.optionsView.getItems().removeAll(this.optionsView.getItems());
        for (String option : this.options.get())
        {
            Label label = new Label(option);
            label.getStyleClass().add("autocompletion-label");
            label.setFont(new Font("Roboto Medium", 15.0));
            this.optionsView.getItems().add(label);
        }
    }

    private int indexOfSelectedItem()
    {
        Label selectedItem = this.optionsView.getSelectionModel().getSelectedItem();
        if (selectedItem == null)
            return -1;
        return this.optionsView.getItems().indexOf(selectedItem);
    }

    public void addRandomOptions(String id)
    {
        List<String> options = new ArrayList<>();
        for (int i = 0; i < 100; i++)
        {
            String text = String.format("%s #%d", id, i);
            options.add(text);
        }
        this.setOptions(options);
    }
}
