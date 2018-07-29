package io.orbit.ui.editorui;

import io.orbit.api.text.CodeEditor;
import io.orbit.controllers.OCodeEditorController;
import io.orbit.text.TextEditorPane;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;

import java.io.File;

/**
 * Created by Tyler Swann on Sunday July 22, 2018 at 15:47
 */
public class EditorTab extends Tab
{
    public static final String DEFAULT_STYLE_CLASS = "editor-tab";
    private CodeEditor editor;
    private OCodeEditorController controller;
    private File file;

    public EditorTab(File file)
    {
        super(file.getName());
        this.editor = new CodeEditor(file);
        this.controller = new OCodeEditorController(this.editor);
        this.file = file;
        TextEditorPane pane = new TextEditorPane(this.controller);
        this.setContent(pane);
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        // TODO - Add events to tabs
//        Label label = new Label(file.getName());
//        label.setAlignment(Pos.CENTER);
//        label.getStyleClass().addAll(this.getStyleClass());
//        label.setPrefHeight(50.0);
//        this.setGraphic(label);
//        label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> System.out.println("click"));
    }

    public OCodeEditorController getController() { return controller; }
    public CodeEditor getEditor() { return editor; }
    public File getFile() { return file; }
}
