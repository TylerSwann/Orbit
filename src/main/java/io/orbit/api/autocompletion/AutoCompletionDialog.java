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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Swann on Wednesday May 30, 2018 at 14:38
 */
public class AutoCompletionDialog extends PopupControl
{
    private ListView<Label> optionsView;
    private Node owner;
    private static final double xOffset = 15.0;
    private static final double yOffset = 0.0;

    private ObservableList<AutoCompletionBase> options = FXCollections.observableArrayList();
    public ObservableList<AutoCompletionBase> getOptions() { return options; }

    public AutoCompletionDialog(List<? extends AutoCompletionBase> options, Node owner)
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


    private void registerListeners()
    {
        this.options.addListener((ListChangeListener<AutoCompletionBase>) c -> this.updateItems());
        this.optionsView.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER)
            {
                AutoCompletionBase selectedOption = (AutoCompletionBase) this.optionsView.getSelectionModel().getSelectedItem().getUserData();
                this.fireEvent(new AutoCompletionEvent(AutoCompletionEvent.OPTION_WAS_SELECTED, this, this, selectedOption));
            }
        });
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

    private void updateItems()
    {
        if (this.optionsView.getItems().size() > 0)
            this.optionsView.getItems().removeAll(this.optionsView.getItems());
        for (AutoCompletionBase option : this.options)
        {
            Label label = new Label(option.getText());
            label.setUserData(option);
            label.getStyleClass().add("autocompletion-label");
            label.setFont(new Font("Roboto Medium", 15.0));
            this.optionsView.getItems().add(label);
        }
    }

    public void addRandomOptions(String id)
    {
        List<AutoCompletionOption> options = new ArrayList<>();
        for (int i = 0; i < 100; i++)
        {
            String text = String.format("%s #%d", id, i);
            options.add(new AutoCompletionOption(text, text));
        }
        this.options.addAll(options);
    }
}
