package io.orbit.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import io.orbit.App;
import io.orbit.api.text.CodeEditor;
import io.orbit.text.OrbitEditor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.IndexRange;
import javafx.scene.control.PopupControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import org.reactfx.EventStream;
import org.reactfx.EventStreams;
import org.reactfx.Subscription;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Tyler Swann on Sunday April 29, 2018 at 14:43
 */
public class FindAndReplace
{
    @FXML private VBox root;
    @FXML private JFXTextField search;
    @FXML private JFXButton clearButton;
    @FXML private JFXButton nextButton;
    @FXML private JFXButton previousButton;
    @FXML private JFXCheckBox matchBox;
    @FXML private JFXCheckBox regexBox;
    @FXML private JFXButton closeButton;
    @FXML private JFXButton dropDownButton;
    @FXML private HBox replacePane;
    @FXML private JFXTextField replace;
    @FXML private JFXButton replaceButton;
    @FXML private JFXButton replaceAllButton;
    private Runnable onCloseRequest = () -> {};
    private boolean useRegex = false;
    private OrbitEditor editor;
    private EventStream<String> searchTextChange;
    private Subscription subscription;
    private List<IndexRange> matchRanges = new ArrayList<>();
    private int currentIndex = 0;
    private final RequiredFieldValidator regexValidator = new RequiredFieldValidator();
    private final RequiredFieldValidator noMatchesValidator = new RequiredFieldValidator();
    private VirtualizedScrollPane<CodeEditor> scrollPane;
    private boolean replaceIsShowing = true;
    public static FindAndReplace controller;
    public Mode mode = Mode.FIND;

    public enum Mode
    {
        FIND,
        FIND_REPLACE
    }


    public static VBox load(CodeEditor editor, boolean withReplaceShowing, VirtualizedScrollPane<CodeEditor> scrollPane)
    {
        try
        {
            URL findURL = App.class.getClassLoader().getResource("views/FindAndReplaceDialog.fxml");
            assert findURL != null;
            FXMLLoader loader = new FXMLLoader(findURL);
            VBox root = loader.load();
            controller = loader.getController();
            controller.editor = (OrbitEditor) editor;
            controller.scrollPane = scrollPane;
            controller.registerListeners(withReplaceShowing);
            return root;
        }
        catch (Exception ex) {  ex.printStackTrace(); }
        return null;
    }

    // TODO - scroll to found matches
    public void initialize()
    {
        toggleReplacePane();
        this.searchTextChange = EventStreams.valuesOf(this.search.textProperty());
        this.clearButton.setText(null);
        this.clearButton.setGraphic(new FontIcon(FontAwesomeSolid.TIMES_CIRCLE));
        this.nextButton.setText(null);
        this.nextButton.setGraphic(new FontIcon(FontAwesomeSolid.ARROW_DOWN));
        this.previousButton.setText(null);
        this.previousButton.setGraphic(new FontIcon(FontAwesomeSolid.ARROW_UP));
        this.closeButton.setGraphic(new FontIcon(FontAwesomeSolid.TIMES));
        this.dropDownButton.setText(null);
        this.dropDownButton.setGraphic(new FontIcon(FontAwesomeSolid.ARROW_RIGHT));
        double scaleX = 1.50;
        double scaleY = 1.50;
        this.dropDownButton.getGraphic().setScaleY(scaleY);
        this.dropDownButton.getGraphic().setScaleX(scaleX);
        this.clearButton.getGraphic().setScaleY(scaleY);
        this.clearButton.getGraphic().setScaleX(scaleX);
        this.nextButton.getGraphic().setScaleY(scaleY);
        this.nextButton.getGraphic().setScaleX(scaleX);
        this.nextButton.setOnAction(event -> this.next());
        this.previousButton.setOnAction(event -> this.previous());
        this.previousButton.getGraphic().setScaleY(scaleY);
        this.previousButton.getGraphic().setScaleX(scaleX);
        this.closeButton.getGraphic().setScaleY(scaleY);
        this.closeButton.getGraphic().setScaleX(scaleX);
        regexValidator.setMessage("Invalid Regex Pattern");
        noMatchesValidator.setMessage("No Matches Found!");
    }

    public void toggleReplacePane()
    {
        if (this.replaceIsShowing)
        {
            mode = Mode.FIND;
            this.root.getChildren().remove(this.replacePane);
            this.root.setPrefHeight(this.root.getPrefHeight() / 2.0);
            this.dropDownButton.setGraphic(new FontIcon(FontAwesomeSolid.ARROW_RIGHT));
        }
        else
        {
            mode = Mode.FIND_REPLACE;
            this.root.setPrefHeight(this.root.getPrefHeight() * 2.0);
            this.root.getChildren().add(this.replacePane);
            this.dropDownButton.setGraphic(new FontIcon(FontAwesomeSolid.ARROW_DOWN));
        }
        this.dropDownButton.getGraphic().setScaleY(1.50);
        this.dropDownButton.getGraphic().setScaleX(1.50);
        this.replaceIsShowing = !this.replaceIsShowing;
    }

    private void registerListeners(boolean withReplaceShowing)
    {

         this.subscription = this.searchTextChange
                                 .successionEnds(Duration.ofMillis(500))
                                 .subscribe(text -> {
                                     if (!this.search.getValidators().isEmpty())
                                         this.search.getValidators().clear();
                                     this.find(text);
                                 });
         this.search.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
             if (event.getCode() == KeyCode.BACK_SPACE)
                 this.editor.forceReHighlighting();
         });
        this.closeButton.setOnAction(event -> {
            this.editor.forceReHighlighting();
            this.subscription.unsubscribe();
            this.onCloseRequest.run();
        });
        this.clearButton.setOnAction(event -> this.search.setText(null));
        this.matchBox.setSelected(true);
        this.matchBox.setOnAction(event -> {
            this.useRegex = false;
            this.regexBox.setSelected(false);
            this.matchBox.setSelected(true);
        });
        this.regexBox.setOnAction(event -> {
            this.useRegex = true;
            this.matchBox.setSelected(false);
            this.regexBox.setSelected(true);
        });
        this.dropDownButton.setOnAction(event -> {
            this.toggleReplacePane();
            if (replaceIsShowing)
                AnchorPane.setTopAnchor(scrollPane, 88.0);
            else
                AnchorPane.setTopAnchor(scrollPane, 50.0);
        });
        this.replaceButton.setOnAction(event -> {
            if (!this.replaceIsShowing)
                throw new RuntimeException("Replace pane is not showing, yet replace button was clicked");
            this.editor.pauseHighlighting();
            this.replace(this.matchRanges.get(this.currentIndex));
            this.next();
        });
        this.replaceAllButton.setOnAction(event -> {
            if (!this.replaceIsShowing)
                throw new RuntimeException("Replace pane is not showing, yet replace all button was clicked");
            while (this.matchRanges.size() > 0)
                this.replace(this.matchRanges.get(0));
        });
        if (withReplaceShowing)
            toggleReplacePane();
    }

    private void replace(IndexRange range)
    {
        this.matchRanges.remove(range);
        this.editor.deleteText(range);
        this.editor.replaceText(range.getStart(), range.getStart(), this.replace.getText());
        this.find(this.search.getText());
    }

    private void find(String input)
    {
        if (input == null || input.isEmpty())
        {
            this.editor.forceReHighlighting();
            return;
        }
        this.matchRanges.clear();
        String regexPattern = useRegex ? input : String.format("(%s)", input);
        Pattern pattern;

        try
        {
            pattern = Pattern.compile(regexPattern);
        }
        catch (PatternSyntaxException ex)
        {
            this.search.getValidators().add(this.regexValidator);
            return;
        }

        Matcher matcher = pattern.matcher(this.editor.getText());
        while (matcher.find())
        {
            int start = matcher.start();
            int end = matcher.end();
            this.matchRanges.add(new IndexRange(start, end));
            this.editor.clearStyle(start, end);
            this.editor.setStyleClass(start, end, "highlighted");
        }
        if (matchRanges.size() > 0)
            this.goToMatch(0);
        else
            this.search.getValidators().add(this.noMatchesValidator);
        if (matchRanges.size() > 1)
            this.nextButton.setDisable(false);
    }

    private void next()
    {
        if (this.currentIndex < this.matchRanges.size())
            this.setHighlighted(this.matchRanges.get(this.currentIndex));
        else
            return;
        if (this.currentIndex + 1 > this.matchRanges.size() - 1)
            this.currentIndex = 0;
        else
            this.currentIndex++;
        IndexRange next = this.matchRanges.get(this.currentIndex);
        this.setSelected(next);
    }

    private void previous()
    {
        if (this.currentIndex >= 0)
            this.setHighlighted(this.matchRanges.get(this.currentIndex));
        else
            return;
        if (this.currentIndex - 1 < 0)
            this.currentIndex = this.matchRanges.size() - 1;
        else
            this.currentIndex--;
        IndexRange previous = this.matchRanges.get(this.currentIndex);
        this.setSelected(previous);
    }

    private void goToMatch(int matchNumber)
    {
        this.currentIndex = matchNumber;
        IndexRange match = this.matchRanges.get(matchNumber);
        this.setSelected(match);
    }

    private void setHighlighted(IndexRange range)
    {
        this.editor.clearStyle(range.getStart(), range.getEnd());
        this.editor.setStyleClass(range.getStart(), range.getEnd(), "highlighted");
    }

    private void setSelected(IndexRange range)
    {
        this.editor.clearStyle(range.getStart(), range.getEnd());
        this.editor.setStyleClass(range.getStart(), range.getEnd(), "highlight-selected");
    }

    public void setOnCloseRequest(Runnable closeRequest)
    {
        Objects.requireNonNull(closeRequest);
        this.onCloseRequest = closeRequest;
    }
}
