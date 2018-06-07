package io.orbit.ui;

import io.orbit.api.event.AutoCompletionModalEvent;
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
import io.orbit.api.autocompletion.AutoCompletionOption;

/**
 * Created by Tyler Swann on Wednesday May 30, 2018 at 14:38
 */
public class AutoCompletionModal extends PopupControl
{
    private ListView<Label> optionsView;
    private Node owner;
    private static final double xOffset = 15.0;
    private static final double yOffset = 0.0;

    private ObservableList<AutoCompletionOption> options = FXCollections.observableArrayList();
    public ObservableList<AutoCompletionOption> getOptions() { return options; }

    public AutoCompletionModal(List<AutoCompletionOption> options, Node owner)
    {
        this(owner);
        this.options.addAll(options);
    }
    public AutoCompletionModal(Node owner)
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
        this.optionsView.setStyle("-fx-background-radius: 5px; " +
                                  "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.5), 10.0, 0.0, 1.5, 2.5);" +
                                  "-fx-background-color: white;");
    }


    private void registerListeners()
    {
        this.options.addListener((ListChangeListener<AutoCompletionOption>) c -> this.updateItems());
        this.optionsView.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER)
            {
                AutoCompletionOption selectedOption = (AutoCompletionOption) this.optionsView.getSelectionModel().getSelectedItem().getUserData();
                this.fireEvent(new AutoCompletionModalEvent(AutoCompletionModalEvent.OPTION_WAS_SELECTED, this, this, selectedOption));
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
        for (AutoCompletionOption option : this.options)
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
