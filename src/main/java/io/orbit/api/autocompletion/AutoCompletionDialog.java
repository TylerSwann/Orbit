package io.orbit.api.autocompletion;

import io.orbit.api.text.CodeEditor;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PopupControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by Tyler Swann on Wednesday May 30, 2018 at 14:38
 */
public class AutoCompletionDialog<T> extends PopupControl
{
    private static final double xOffset = 20.0;
    private static final double yOffset = 0.0;

    public void setCellFactory(Function<T, String> cellFactory) { this.cellFactory = cellFactory; }
    public Function<T, String> getCellFactory() { return this.cellFactory; }
    private Function<T, String> cellFactory;

    private ObservableList<T> options = FXCollections.observableArrayList();
    public ObservableList<T> getOptions() { return options; }

    private SimpleObjectProperty<Optional<T>> selectedOption = new SimpleObjectProperty<>(Optional.empty());
    public ObservableValue<Optional<T>> selectedOptionProperty() {  return selectedOption;  }
    public Optional<T> getSelectedOption() { return selectedOption.get(); }

    private ListView<Label> optionsView;
    private Node owner;
    private AnchorPane root;


    public AutoCompletionDialog(List<T> options, Node owner)
    {
        this(owner);
        this.options.addAll(options);
    }

    public AutoCompletionDialog(Node owner)
    {
        this.owner = owner;
        this.optionsView = new ListView<>();
        this.setAutoHide(true);
        this.optionsView.setFixedCellSize(41.5);
        double width = 500.0;
        double height = 200.0;
        this.setPrefHeight(height);
        this.optionsView.setPrefHeight(height);
        this.optionsView.setMinWidth(ListView.USE_PREF_SIZE);
        this.optionsView.setMaxWidth(ListView.USE_PREF_SIZE);
        this.setMinWidth(PopupControl.USE_PREF_SIZE);
        this.setMaxHeight(PopupControl.USE_PREF_SIZE);
        this.optionsView.setPrefWidth(width);
        this.setPrefWidth(width);
        AnchorPane.setTopAnchor(optionsView, 0.0);
        AnchorPane.setBottomAnchor(optionsView, 0.0);
        AnchorPane.setLeftAnchor(optionsView, 0.0);
        AnchorPane.setRightAnchor(optionsView, 0.0);
        this.root = new AnchorPane();
        root.setPrefSize(width, height);
        root.getChildren().add(this.optionsView);
        updateItems();
        this.getScene().setRoot(root);
        registerListeners();
        this.setStyle("-fx-background-color: transparent;");
        this.optionsView.getStyleClass().add("auto-completion-dialog");
        if (owner instanceof CodeEditor)
            ((CodeEditor)owner).requestFollowCaret();
    }


    public void show(double x, double y)
    {
        if (!this.isShowing())
        {
            super.show(this.owner.getScene().getWindow());
            this.optionsView.getSelectionModel().selectFirst();
            this.optionsView.requestFocus();
        }
        this.setX(x + xOffset);
        this.setY(y + yOffset);
        this.optionsView.getSelectionModel().selectFirst();
    }

    @Override
    public void hide()
    {
        if (this.isShowing())
            super.hide();
    }

    public void updateOptions(List<T> options)
    {
        this.getOptions().removeAll(this.getOptions());
        this.getOptions().addAll(options);
    }

    private void registerListeners()
    {
        this.options.addListener((ListChangeListener<T>) c -> this.updateItems());
        this.optionsView.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER)
            {
                if (this.optionsView.getSelectionModel().getSelectedItem() != null && this.optionsView.getSelectionModel().getSelectedItem().getUserData() != null)
                {
                    @SuppressWarnings("unchecked") T selectedOption = (T) this.optionsView.getSelectionModel().getSelectedItem().getUserData();
                    this.selectedOption.set(Optional.of(selectedOption));
                }
            }
        });
        this.options.addListener((ListChangeListener<T>) c -> {
            double maxWidth = 0.0;
            for (Label label : this.optionsView.getItems())
            {
                double computedWidth = label.getText().length() * label.getFont().getSize();
                if (computedWidth > maxWidth)
                    maxWidth = computedWidth;
            }
            maxWidth += 100.0;
            this.root.setPrefWidth(maxWidth / 2.0);
            if (this.optionsView.getItems().size() > 5)
                this.root.setPrefHeight(300.0);
            else
                this.root.setPrefHeight(200.0);
        });
    }

    private void updateItems()
    {
        if (this.optionsView.getItems().size() > 0)
            this.optionsView.getItems().removeAll(this.optionsView.getItems());
        for (T option : this.options)
        {
            String text = this.cellFactory == null ? option.toString() : this.cellFactory.apply(option);
            Label label = new Label(text);
            label.setUserData(option);
            label.getStyleClass().add("autocompletion-label");
            label.setFont(new Font("Roboto Medium", 15.0));
            this.optionsView.getItems().add(label);
        }
    }
}
