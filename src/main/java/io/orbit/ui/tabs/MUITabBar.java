package io.orbit.ui.tabs;

import io.orbit.ui.MUIIconButton;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.List;

/**
 * Created by Tyler Swann on Sunday August 19, 2018 at 13:02
 */
public class MUITabBar extends AnchorPane
{
    public static final String DEFAULT_STYLE_CLASS = "mui-tab-bar";
    public static final String INDICATOR_STYLE_CLASS = "indicator";

    private HBox tabsContainer;
    private AnchorPane container;
    private VBox leftArrowContainer;
    private VBox rightArrowContainer;
    private MUIIconButton leftArrow;
    private MUIIconButton rightArrow;
    private Pane indicator;

    private final double tabBarHeight = 40.0;
    private final double indicatorHeight = 2.0;
    private boolean arrowsAreActive = false;
    private Scene scene;
    private InvalidationListener windowWidthChangeEvent;

    public ObservableList<MUITab> getTabs() { return tabs; }
    private ObservableList<MUITab> tabs = FXCollections.observableArrayList();

    private SimpleObjectProperty<MUITab> selectedTab = new SimpleObjectProperty<>();
    public ObservableValue<MUITab> selectedTabProperty() {  return selectedTab;  }
    public MUITab getSelectedTab() {  return this.selectedTab.get();  }

    public MUITabBar(List<MUITab> tabs)
    {
        this();
        this.getTabs().addAll(tabs);
    }

    public MUITabBar()
    {
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.setPrefHeight(tabBarHeight);
        build();
        registerListeners();
    }

    private void build()
    {
        this.container = new AnchorPane();
        this.container.setPrefHeight(tabBarHeight);
        this.tabsContainer = new HBox();
        this.tabsContainer.setStyle("-fx-background-color: transparent;");
        this.tabsContainer.setAlignment(Pos.CENTER_LEFT);
        AnchorPane.setTopAnchor(this.tabsContainer, 0.0);
        AnchorPane.setBottomAnchor(this.tabsContainer, indicatorHeight);
        AnchorPane.setLeftAnchor(this.tabsContainer, 0.0);
        AnchorPane.setTopAnchor(this.container, 0.0);
        AnchorPane.setBottomAnchor(this.container, 0.0);
        AnchorPane.setLeftAnchor(this.container, 0.0);
        this.leftArrowContainer = new VBox();
        this.rightArrowContainer = new VBox();
        this.leftArrowContainer.setPrefWidth(tabBarHeight);
        this.rightArrowContainer.setPrefWidth(tabBarHeight);
        AnchorPane.setTopAnchor(this.rightArrowContainer, 0.0);
        AnchorPane.setBottomAnchor(this.rightArrowContainer, indicatorHeight);
        AnchorPane.setRightAnchor(this.rightArrowContainer, 0.0);
        AnchorPane.setTopAnchor(this.leftArrowContainer, 0.0);
        AnchorPane.setBottomAnchor(this.leftArrowContainer, indicatorHeight);
        AnchorPane.setLeftAnchor(this.leftArrowContainer, 0.0);
        leftArrow = new MUIIconButton(FontAwesomeSolid.ARROW_ALT_CIRCLE_LEFT);
        rightArrow = new MUIIconButton(FontAwesomeSolid.ARROW_ALT_CIRCLE_RIGHT);
        this.leftArrowContainer.getChildren().add(leftArrow);
        this.rightArrowContainer.getChildren().add(rightArrow);
        this.leftArrowContainer.getStyleClass().add("left");
        this.rightArrowContainer.getStyleClass().add("right");
        this.leftArrowContainer.setAlignment(Pos.CENTER);
        this.rightArrowContainer.setAlignment(Pos.CENTER);
        this.indicator = new Pane();
        this.indicator.setPrefHeight(indicatorHeight);

        this.indicator.getStyleClass().add(INDICATOR_STYLE_CLASS);
        AnchorPane.setBottomAnchor(this.indicator, 0.0);
        this.getChildren().add(this.container);
        this.container.getChildren().addAll(this.tabsContainer, this.indicator);
        this.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.GREY, 10.0, 0.0, 0.0, 2.0));
        this.windowWidthChangeEvent = __ -> Platform.runLater(this::checkWidth);
    }

    private void registerListeners()
    {
        this.container.widthProperty().addListener(__ -> Platform.runLater(this::checkWidth));
        this.selectedTab.addListener(__ -> {
            this.indicator.setPrefWidth(this.selectedTab.get().getWidth());
            TranslateTransition transition = new TranslateTransition(Duration.millis(300.0), this.indicator);
            transition.setFromX(this.indicator.getTranslateX());
            transition.setToX(this.selectedTab.get().getLayoutX());
            transition.play();
        });
        this.tabs.addListener((ListChangeListener<MUITab>) change -> {
            if (this.selectedTab.get() == null)
                Platform.runLater(() -> this.selectedTab.set(this.getTabs().get(0)));
            while (change.next())
            {
                change.getAddedSubList().forEach(tab -> {
                    this.tabsContainer.getChildren().add(tab);
                    if (!tab.prefHeightProperty().isBound())
                        tab.prefHeightProperty().bind(this.tabsContainer.heightProperty());
                    if (tab.getText() != null)
                        tab.setText(tab.getText().toUpperCase());
                    tab.addEventHandler(MouseEvent.MOUSE_CLICKED, __ -> this.selectedTab.set(tab));
                });
                change.getRemoved().forEach(tab -> {
                    this.tabsContainer.getChildren().remove(tab);
                    if (tab.prefHeightProperty().isBound())
                        tab.prefHeightProperty().unbind();
                });
            }
        });
        leftArrow.setOnAction(__ -> {
            double amount = this.container.getTranslateX() + (this.getTabs().get(0).getWidth() * 3.0);
            if (amount > tabBarHeight)
                amount = tabBarHeight;
            scroll(this.container, amount);
        });
        rightArrow.setOnAction(__ -> {
            double amount = this.container.getTranslateX() - (this.getTabs().get(0).getWidth() * 3.0);
            if ((this.getWidth() - this.container.getWidth()) > amount)
                amount = (this.getWidth() - this.container.getWidth()) - tabBarHeight;
            scroll(this.container, amount);
        });
        this.sceneProperty().addListener(__ -> {
            if (this.scene != null)
                this.scene.widthProperty().removeListener(this.windowWidthChangeEvent);
            if (this.getScene() != null)
            {
                this.scene = this.getScene();
                this.scene.widthProperty().addListener(this.windowWidthChangeEvent);
            }
        });
    }

    private void scroll(Node node, double to)
    {
        if (to == node.getTranslateX())
            return;
        TranslateTransition transition = new TranslateTransition(Duration.millis(500.0), node);
        transition.setFromX(node.getTranslateX());
        transition.setToX(to);
        transition.play();
    }

    private void checkWidth()
    {
        if (this.container.getWidth() > this.getWidth() && !arrowsAreActive)
        {
            this.getChildren().add(this.leftArrowContainer);
            this.getChildren().add(this.rightArrowContainer);
            arrowsAreActive = true;
        }
        else if (!(this.container.getWidth() > this.getWidth()) && arrowsAreActive)
        {
            this.getChildren().remove(this.leftArrowContainer);
            this.getChildren().remove(this.rightArrowContainer);
            arrowsAreActive = false;
        }
    }
}