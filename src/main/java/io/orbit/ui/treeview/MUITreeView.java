package io.orbit.ui.treeview;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.reactfx.EventStreams;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class MUITreeView<T> extends ScrollPane
{
    private static final String DEFAULT_STYLE_CLASS = "mui-tree-view";

    private ObservableList<MUITreeItem<T>> tree = FXCollections.observableArrayList();
    private final ObservableList<MUITreeItem<T>> branches = FXCollections.observableArrayList();
    public ObservableList<MUITreeItem<T>> getBranches() { return branches; }

    private final SimpleObjectProperty<List<MUITreeItem<T>>> selectedBranches = new SimpleObjectProperty<>(Collections.emptyList());
    public ObservableValue<List<MUITreeItem<T>>> selectedBranchesProperty() { return selectedBranches; }
    public List<MUITreeItem<T>> getSelectedItems() { return selectedBranches.get(); }

    private final SimpleObjectProperty<MUITreeItem<T>> selectedBranch = new SimpleObjectProperty<>();
    public ObservableValue<MUITreeItem<T>> selectedBranchProperty() { return selectedBranch; }
    public MUITreeItem<T> getSelectedBranch() { return selectedBranch.get(); }

    private VBox container;
    private AnchorPane root;
    private Function<T, String> cellFactory = Object::toString;
    private Function<MUITreeItem<T>, Node> graphicFactory;

    public MUITreeView() { this(Collections.emptyList()); }
    public MUITreeView(MUITreeItem<T>... branches) { this(Arrays.asList(branches)); }

    public MUITreeView(List<MUITreeItem<T>> branches)
    {
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.getStyleClass().add("edge-to-edge");
        this.container = new VBox();
        this.root = new AnchorPane();
        this.branches.addAll(branches);
        this.build();
        this.container.getChildren().clear();
        this.branches.forEach(this::add);
        registerListeners();
    }

    private void build()
    {
        AnchorPane.setTopAnchor(this.container, 0.0);
        AnchorPane.setLeftAnchor(this.container, 0.0);
        AnchorPane.setRightAnchor(this.container, 0.0);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        AnchorPane root = new AnchorPane();
        container.prefWidthProperty().bind(this.widthProperty());
        root.getChildren().add(this.container);
        this.setContent(root);
        this.setPrefSize(300.0, 600.0);
        this.container.setPrefHeight(VBox.USE_PREF_SIZE);
        this.container.setMaxHeight(VBox.USE_PREF_SIZE);
        this.container.setMinHeight(VBox.USE_PREF_SIZE);
        this.container.setPrefHeight(this.computeHeight());
        this.root.setMinHeight(AnchorPane.USE_PREF_SIZE);
        this.root.setPrefHeight(AnchorPane.USE_PREF_SIZE);
        this.root.setMaxHeight(AnchorPane.USE_PREF_SIZE);
        this.root.setPrefHeight(this.computeHeight());
        this.container.backgroundProperty().bind(this.backgroundProperty());
    }

    private void registerListeners()
    {
        this.tree.addListener((InvalidationListener) obs -> {
            this.root.setPrefHeight(this.getTree().size() * 50.0);
            this.container.setPrefHeight(this.getTree().size() * 50.0);
        });
        this.branches.addListener((ListChangeListener<MUITreeItem<T>>) change -> {
            while (change.next())
            {
                change.getAddedSubList().forEach(this::add);
                change.getRemoved().forEach(this::remove);
            }
        });
        this.addEventHandler(MUITreeViewEvent.ITEM_SELECTED, event -> {
            MouseEvent mouseEvent = event.getMouseEvent();
            if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() < 2 && !mouseEvent.isControlDown())
            {
                this.deselectIf(item -> item != event.getTarget());
                event.getTarget().setSelected(!event.getTarget().isSelected());
                updateSelected();
            }
            else if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() < 2)
            {
                event.getTarget().setSelected(!event.getTarget().isSelected());
                updateSelected();
            }
            else if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() >= 2)
            {
                event.getTarget().setSelected(true);
                updateSelected();
                this.fireEvent(new MUITreeViewEvent<T>(MUITreeViewEvent.ITEM_DOUBLE_CLICKED, event.getTarget()));
            }
            else if (mouseEvent.getButton() == MouseButton.SECONDARY)
            {
                this.deselectIf(item -> item != event.getTarget());
                event.getTarget().setSelected(true);
                updateSelected();
                this.fireEvent(new MUITreeViewEvent<T>(MUITreeViewEvent.CONTEXT_MENU_REQUEST, event.getMouseEvent(), event.getTarget()));
            }
        });
        this.addEventHandler(MUITreeViewEvent.ITEM_EXPANSION_CHANGE, __ -> Platform.runLater(this::adjustHeight));
        this.tree.addListener((InvalidationListener) (obs) -> Platform.runLater(this::adjustHeight));
    }

    private void adjustHeight()
    {
        this.root.setPrefHeight(computeHeight());
        this.container.setPrefHeight(computeHeight());
    }

    private double computeHeight(List<MUITreeItem<T>> items)
    {
        double height = 0.0;
        for (int i = 0; i < items.size(); i++)
        {
            height += MUITreeItem.ITEM_HEIGHT;
            if (items.get(i).isExpanded())
                height += computeHeight(items.get(i).getBranches());
        }
        return height;
    }

    private double computeHeight()
    {
        double height = 0.0;
        for (int i = 0; i < this.branches.size(); i++)
        {
            height += MUITreeItem.ITEM_HEIGHT;
            if (this.branches.get(i).isExpanded())
                height += this.computeHeight(this.branches.get(i).getBranches());
        }
        return height;
    }

    private void updateSelected()
    {
        List<MUITreeItem<T>> selectedItems = new ArrayList<>();
        this.tree.forEach(branch -> {
            if (branch.isSelected())
                selectedItems.add(branch);
        });
        this.selectedBranches.set(selectedItems);
    }

    private void add(MUITreeItem<T> item)
    {
        item.setIndent(MUITreeItem.INDENT_LEVEL);
        item.setParentView(this);
        item.setCellFactory(this.cellFactory);
        this.container.getChildren().add(item);
        this.tree.add(item);
    }

    private void remove(MUITreeItem<T> item)
    {
        this.container.getChildren().remove(item);
        this.tree.remove(item);
    }

    public void removeFromTree(MUITreeItem<T> item)
    {
        this.branches.forEach(branch -> {
            if (item == branch)
                this.branches.remove(item);
            else
                branch.remove(item);
        });
    }

    public Function<T, String> getCellFactory() { return this.cellFactory; }
    public void setCellFactory(Function<T, String> factory)
    {
        this.cellFactory = factory;
        this.branches.forEach(branch -> branch.setCellFactory(factory));
    }

    public Function<MUITreeItem<T>, Node> getCellGraphicFactory() { return this.graphicFactory; }
    public void setCellGraphicFactory(Function<MUITreeItem<T>, Node> graphicFactory)
    {
        this.graphicFactory = graphicFactory;
        this.branches.forEach(branch -> branch.setCellGraphicFactory(graphicFactory));
    }

    public void deselectAll()
    {
        this.branches.forEach(branch -> branch.setSelected(false));
    }
    public void deselectIf(Predicate<MUITreeItem<T>> condition)
    {
        this.tree.filtered(condition).forEach(item -> item.setSelected(false));
    }

    public void setSelected(int index, boolean selected)
    {
        if (index > this.tree.size())
            throw new IndexOutOfBoundsException("Index selected is greater than size of branches in MUITreeView");
        this.tree.get(index).setSelected(selected);
    }

    public void setSelected(int from, int to, boolean selected)
    {
        if (from >= to || to > this.tree.size())
            throw new IndexOutOfBoundsException(String.format("Branches array is not in bounds of %d -> %d", from, to));
        for (int i = from; i <= to; i++)
            this.tree.get(i).setSelected(selected);
    }


    List<MUITreeItem<T>> getModifiableTree() { return this.tree; }
    protected List<MUITreeItem<T>> getTree() { return Collections.unmodifiableList(this.getModifiableTree()); }
}
