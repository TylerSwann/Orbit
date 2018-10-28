/*
 * <Orbit Editor. An Open Source Text Editor>
 *
 * Copyright (C) 2018 Jordan Swann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.orbit.ui.treeview;

import com.jfoenix.controls.JFXButton;
import io.orbit.ui.MUIIconButton;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import java.util.Objects;
import java.util.function.Function;

public class MUITreeItem<T> extends VBox
{
    private static final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");
    private static final String DEFAULT_STYLE_CLASS = "mui-tree-item";
    static final double ITEM_HEIGHT = 30.0;
    static final double INDENT_LEVEL = 20.0;

    private MUITreeView<T> parentView;
    private MUIIconButton expandButton;
    private HBox header;
    private VBox content;
    private Label label;
    private T value;
    private double indent = INDENT_LEVEL;

    private ObservableList<MUITreeItem<T>> branches = FXCollections.observableArrayList();
    public ObservableList<MUITreeItem<T>> getBranches() { return branches; }

    private SimpleObjectProperty<Boolean> isExpanded = new SimpleObjectProperty<>();
    public ObservableValue<Boolean> expandedProperty() { return isExpanded; }
    public boolean isExpanded() { return isExpanded.get(); }
    public void setExpanded(boolean expanded) { this.isExpanded.set(expanded); }

    private SimpleObjectProperty<Boolean> selected = new SimpleObjectProperty<>(false);
    public ObservableValue<Boolean> selectedProperty() { return selected; }
    public boolean isSelected() { return selected.get(); }
    public void setSelected(boolean selected) { this.selected.set(selected); }

    private final SimpleObjectProperty<Node> graphic = new SimpleObjectProperty<>();
    public Node getGraphic() { return this.graphic.getValue(); }
    public void setGraphic(Node graphic) { this.graphic.set(graphic); }

    private Function<MUITreeItem<T>, Node> graphicFactory;
    public Function<MUITreeItem<T>, Node> getGraphicFactory() { return this.graphicFactory; }

    private Function<T, String> cellFactory;
    public Function<T, String> getCellFactory() { return this.cellFactory; }

    public MUITreeItem(T value) { this(value, null, false); }
    public MUITreeItem(T value, Node graphic) { this(value, graphic, false); }

    public MUITreeItem(T value, Node graphic, boolean expanded)
    {
        this.value = value;
        this.setGraphic(graphic);
        this.label = new Label(value.toString());
        this.content = new VBox();
        this.header = new HBox();
        this.isExpanded.set(expanded);
        this.expandButton = new MUIIconButton(FontAwesomeSolid.CARET_RIGHT);
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.header.getStyleClass().add("mui-tree-header");
        this.header.setSpacing(10.0);
        this.build();
        this.registerListeners();
    }

    private void build()
    {
        this.header.setPrefHeight(ITEM_HEIGHT);
        this.header.setMaxHeight(USE_PREF_SIZE);
        this.header.setMinHeight(USE_PREF_SIZE);
        this.header.setAlignment(Pos.CENTER_LEFT);
        this.setPrefHeight(ITEM_HEIGHT);

        if (this.getGraphic() != null)
            this.header.getChildren().addAll(this.expandButton, this.getGraphic(), this.label);
        else
            this.header.getChildren().addAll(this.expandButton, this.label);
        this.content.getChildren().addAll(this.branches);
        super.getChildren().addAll(this.header, this.content);
        this.checkExpansion();
        if (branches.size() <= 0)
            this.expandButton.setOpacity(0.0);
    }

    private void registerListeners()
    {
        this.header.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if ((event.getTarget() instanceof JFXButton))
                return;
            this.parentView.fireEvent(new MUITreeViewEvent<>(MUITreeViewEvent.ITEM_SELECTED, event, this));
        });
        this.selected.addListener(obs -> this.pseudoClassStateChanged(SELECTED, this.selected.get()));
        this.branches.addListener((ListChangeListener<MUITreeItem<T>>) change -> {
            while (change.next())
            {
                change.getRemoved().forEach(this::remove);
                change.getAddedSubList().forEach(this::add);
            }
            if (branches.size() <= 0)
                this.expandButton.setOpacity(0.0);
            else if (this.expandButton.getOpacity() <= 0.0)
                this.expandButton.setOpacity(1.0);
        });
        this.isExpanded.addListener(obs -> this.checkExpansion());
        this.expandButton.setOnAction(__ -> {
            this.isExpanded.set(!(this.isExpanded.get()));
            if (this.isSelected())
                this.setSelected(true);
        });
        this.graphic.addListener((obs, oldV, newV) -> {
            this.header.getChildren().remove(oldV);
            this.header.getChildren().add(1, newV);
        });
    }

    private void checkExpansion()
    {
        if (this.isExpanded())
        {
            this.content.setScaleY(1.0);
            this.expandButton.getStyleClass().add("expanded");
            this.setMaxHeight(USE_COMPUTED_SIZE);
            this.setMinHeight(USE_COMPUTED_SIZE);
        }
        else
        {
            this.content.setScaleY(0.0);
            this.expandButton.getStyleClass().remove("expanded");
            this.setMaxHeight(USE_PREF_SIZE);
            this.setMinHeight(USE_PREF_SIZE);
        }
        if (this.parentView != null)
            this.parentView.fireEvent(new MUITreeViewEvent<>(MUITreeViewEvent.ITEM_EXPANSION_CHANGE, this));
    }

    void setIndent(double indent)
    {
        this.indent = indent;
        this.content.setPadding(new Insets(0.0, 0.0, 0.0, indent));
        this.branches.forEach(branch -> branch.setIndent(indent + INDENT_LEVEL));
    }

    void setCellFactory(Function<T, String> factory)
    {
        this.cellFactory = factory;
        this.label.setText(factory.apply(this.value));
        this.branches.forEach(branch -> branch.setCellFactory(factory));
    }

    void setCellGraphicFactory(Function<MUITreeItem<T>, Node> graphicFactory)
    {
        this.graphicFactory = graphicFactory;
        Node graphic = graphicFactory.apply(this);
        this.setGraphic(graphic);
        this.branches.forEach(branch -> branch.setCellGraphicFactory(graphicFactory));
    }

    private void add(MUITreeItem<T> item)
    {
        if (this.parentView != null)
        {
            if (item.getParentView() == null)
                item.setParentView(this.parentView);
            this.parentView.getModifiableTree().add(item);
        }
        if (this.cellFactory != null)
            item.setCellFactory(this.cellFactory);
        if (this.graphicFactory != null)
            item.setCellGraphicFactory(this.graphicFactory);
        item.setIndent(this.indent + INDENT_LEVEL);
        this.content.getChildren().add(item);
    }

    void remove(MUITreeItem<T> item)
    {
        if (this.branches.contains(item))
        {
            this.parentView.getModifiableTree().remove(item);
            this.content.getChildren().remove(item);
            return;
        }
        this.branches.forEach(branch -> branch.remove(item));
    }

    public T getValue() { return value; }
    @Override
    public ObservableList<Node> getChildren() { return this.getChildrenUnmodifiable(); }
    MUITreeView<T> getParentView() { return this.parentView; }
    void setParentView(MUITreeView<T> parentView)
    {
        Objects.requireNonNull(parentView);
        if (this.parentView != null)
            return;
        this.parentView = parentView;
        this.parentView.getModifiableTree().addAll(this.branches);
        this.branches.forEach(branch -> branch.setParentView(this.parentView));
        this.prefWidthProperty().bind(this.parentView.widthProperty());
    }
}
