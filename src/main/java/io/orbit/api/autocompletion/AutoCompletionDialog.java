package io.orbit.api.autocompletion;

import io.orbit.api.event.AutoCompletionEvent;
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
import javafx.scene.text.Font;

import java.util.List;
import java.util.function.Function;

/**
 * Created by Tyler Swann on Wednesday May 30, 2018 at 14:38
 */
public class AutoCompletionDialog<T> extends PopupControl
{
    private ListView<Label> optionsView;
    private Node owner;
    private static final double xOffset = 15.0;
    private static final double yOffset = 0.0;

    public void setCellFactory(Function<T, String> cellFactory) { this.cellFactory = cellFactory; }
    public Function<T, String> getCellFactory() { return this.cellFactory; }
    private Function<T, String> cellFactory;

    private ObservableList<T> options = FXCollections.observableArrayList();
    public ObservableList<T> getOptions() { return options; }

    /*
     * TODO - give this class a generic type parameter in order to allow displaying of custom classes.
     * TODO - create a method call setCellFactory for converting generic types into their string format
     * */

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
        double width = 700.0;
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
        this.optionsView.getStyleClass().add("auto-completion-dialog");
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
    }

    @Override
    public void hide()
    {
        super.hide();
    }

    private void registerListeners()
    {
        this.options.addListener((ListChangeListener<T>) c -> this.updateItems());
        this.optionsView.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER)
            {
                T selectedOption = (T) this.optionsView.getSelectionModel().getSelectedItem().getUserData();
                this.fireEvent(new AutoCompletionEvent<>(AutoCompletionEvent.OPTION_WAS_SELECTED, this, this, selectedOption));
            }
        });
    }

    private void updateItems()
    {
        if (this.optionsView.getItems().size() > 0)
            this.optionsView.getItems().removeAll(this.optionsView.getItems());
        for (T option : this.options)
        {
            // Check if cell factory has been created here.
            String text = this.cellFactory == null ? option.toString() : this.cellFactory.apply(option);
            Label label = new Label(text);
            label.setUserData(option);
            label.getStyleClass().add("autocompletion-label");
            label.setFont(new Font("Roboto Medium", 15.0));
            this.optionsView.getItems().add(label);
        }
    }
}
