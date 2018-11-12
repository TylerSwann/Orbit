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
 *
 */
package io.orbit.webtools.javascript.autocompletion;

import io.orbit.api.autocompletion.AutoCompletionDialog;
import io.orbit.api.text.CodeEditor;
import io.orbit.webtools.javascript.typedefs.parsing.Method;
import io.orbit.webtools.javascript.typedefs.parsing.Property;
import io.orbit.webtools.javascript.typedefs.parsing.Type;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By: Tyler Swann.
 * Date: Thursday, Nov 08, 2018
 * Time: 3:12 PM
 * Website: https://orbiteditor.com
 */
public class JavaScriptAutoCompleter
{
    private static final String ID_PATTERN = "[a-zA-Z0-9]+";
    private static final String FN_PATTERN = "[a-zA-Z0-9]+\\(.*\\)";
    private AutoCompletionDialog<JSOption> dialog;
    private ProjectScope scope;
    private CodeEditor editor;

    public JavaScriptAutoCompleter(CodeEditor editor, ProjectScope scope)
    {
        this.editor = editor;
        this.scope = scope;
        this.dialog = new AutoCompletionDialog<>(editor);
        this.dialog.setCellFactory(option -> option.displayText);
        this.registerListeners();
    }

    private void registerListeners()
    {
        this.editor.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> this.dialog.hide());
        this.editor.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            switch (event.getCode())
            {
                case ENTER: break;
                case BACK_SPACE:
                case COLON:
                case SEMICOLON:
                case END:
                case HOME:
                    this.dialog.hide();
                    break;
                case PERIOD:
                    this.dotWasTyped(event);
                    break;
                default:
                    this.keyWasReleased(event);
                    break;
            }
        });
        this.dialog.selectedOptionProperty().addListener((obs, oldValue, newValue) -> {
            newValue.ifPresent(option -> {
                String textLeftOfCaret = this.textLeftOfCaret().trim();
                int caretPos = this.editor.getFocusPosition().caretPositionInDocument;
                String insertionText = option.insertionText.replaceFirst(textLeftOfCaret, "");
                this.editor.insertText(caretPos, insertionText);
                updateDialogPosition();
            });
        });
    }

    private void dotWasTyped(KeyEvent event)
    {
        String textLeftOfCaret = this.textLeftOfCaret();
        String[] pieces = textLeftOfCaret.split("\\.");
        String first;
        if (pieces == null || pieces.length <= 0)
            return;
        first = pieces[0];
        System.out.println(first);
        Type type = this.typeOfIdentifier(first);
        for (int i = 1; i < pieces.length; i++)
        {
            String piece = pieces[i];
            System.out.println(piece);
            if (piece.matches(FN_PATTERN))
            {
                Method method = this.methodWithName(piece, type.getMethods());
                if (method != null)
                {
                    type = method.getReturnType();
                    continue;
                }
            }
            if (piece.matches(ID_PATTERN))
            {
                Property property = this.propertyWithName(piece, type.getProperties());
                if (property != null)
                    type = property.getType();
            }
        }
        if (type == Type.VOID)
        {
            this.dialog.hide();
            return;
        }
        if (type != null)
        {
            System.out.println("Options For: " + type.getName());
            List<JSOption> options = JSOption.from(type);
            this.dialog.updateOptions(options);
            this.updateDialogPosition();
        }
    }

    private void keyWasReleased(KeyEvent event)
    {
        String textLeftOfCaret = this.textLeftOfCaret();
        List<JSOption> options = optionsFor(textLeftOfCaret);
        if (options.isEmpty())
            return;
        this.dialog.updateOptions(options);
        updateDialogPosition();
    }

    private List<JSOption> optionsFor(String text)
    {
        List<JSOption> options = new ArrayList<>();
        if (text == null || text.equals("") || text.matches("^\\s+$"))
            return options;
        for (String varName : this.scope.library.variables.keySet())
        {
            if (varName.startsWith(text))
                options.add(JSOption.from(this.scope.library.variables.get(varName)));
        }
        return options;
    }

    private void updateDialogPosition()
    {
        this.editor.getCaretBounds().ifPresent(bounds -> this.dialog.show(bounds.getMinX(), bounds.getMinY()));
    }

    private String textLeftOfCaret()
    {
        String textLeftOfCaret = this.editor.getTextLeftOfCaret();
        textLeftOfCaret = textLeftOfCaret == null ? "" : textLeftOfCaret;
        return textLeftOfCaret;
    }
    private Type typeOfIdentifier(String id)
    {
        if (this.scope.library.variables.get(id) != null)
            return this.scope.library.variables.get(id).getType();
        else if (this.scope.library.interfaces.get(id) != null)
            return this.scope.library.interfaces.get(id);
        else if (this.scope.library.classes.get(id) != null)
            return this.scope.library.classes.get(id);
        return Type.ANY;
    }

    private Property propertyWithName(String name, List<Property> properties)
    {
        for (Property property : properties)
        {
            if (property.getName().equals(name))
                return property;
        }
        return null;
    }

    private Method methodWithName(String name, List<Method> methods)
    {
        for (Method method : methods)
        {
            if (method.getName().equals(name))
                return method;
        }
        return null;
    }
}


