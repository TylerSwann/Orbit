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

import com.google.gson.Gson;
import io.orbit.api.autocompletion.AutoCompletionDialog;
import io.orbit.api.text.CodeEditor;
import io.orbit.util.Tuple;
import io.orbit.webtools.javascript.typedefs.parsing.Method;
import io.orbit.webtools.javascript.typedefs.parsing.Property;
import io.orbit.webtools.javascript.typedefs.parsing.Type;
import io.orbit.webtools.javascript.typedefs.parsing.Variable;
import javafx.application.Platform;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        Platform.runLater(() -> {
            try
            {
                Gson gson = new Gson();
                int index = 0;
                for (Property property : scope.library.interfaces.get("String").getProperties())
                {
                    if (property.getName().equals("length"))
                    {
                        index = scope.library.interfaces.get("String").getProperties().indexOf(property);
                        break;
                    }
                }
                byte[] data = gson.toJson(scope.library.interfaces.get("String")).getBytes();
                Files.write(Paths.get(new File("C:\\Users\\TylersDesktop\\Downloads\\es5.json").getPath()), data);
            }
            catch (IOException e) { e.printStackTrace(); }
        });
        this.registerListeners();
    }

    private void registerListeners()
    {
        System.out.println("load");
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
        this.dialog.selectedOptionProperty().addListener((obs, oldValue, newValue) -> newValue.ifPresent(option -> {
            String textLeftOfCaret = this.textLeftOfCaret().trim();
            int caretPos = this.editor.getFocusPosition().caretPositionInDocument;
            String insertionText = option.insertionText.replaceFirst(textLeftOfCaret, "");
            String[] pieces = textLeftOfCaret.split("\\.");
            if (pieces != null && pieces.length > 0)
                insertionText = option.insertionText.replaceFirst(pieces[pieces.length - 1], "");
            this.editor.insertText(caretPos, insertionText);
            if (!option.isFunction || option.type != Type.VOID)
                updateDialogPosition();
            else
                dialog.hide();
        }));
    }

    private void dotWasTyped(KeyEvent event)
    {
        String textLeftOfCaret = this.textLeftOfCaret();
        Type type = lastTypeOfFragment(textLeftOfCaret).second;
        if (type == Type.VOID || type == null)
        {
            this.dialog.hide();
            return;
        }

        List<JSOption> options = JSOption.from(type);
        this.dialog.updateOptions(options);
        this.updateDialogPosition();
    }

    private void keyWasReleased(KeyEvent event)
    {
        String textLeftOfCaret = this.textLeftOfCaret();
        List<JSOption> options = optionsForFragment(textLeftOfCaret);
        if (options.isEmpty())
        {
            dialog.hide();
            return;
        }
        this.dialog.updateOptions(options);
        updateDialogPosition();
    }

    private List<JSOption> optionsForFragment(String text)
    {
        List<JSOption> options = new ArrayList<>();
        text = text == null ? "" : text;
        String[] pieces = text.split("\\.");
        if (text.equals("") || text.matches("^\\s+$") || pieces.length <= 0)
            return options;
        if (pieces.length == 1)
        {
            for (String varName : this.scope.library.variables.keySet())
            {
                if (varName.startsWith(text))
                    options.add(JSOption.from(this.scope.library.variables.get(varName)));
            }
            for (String fnName : this.scope.library.functions.keySet())
            {
                if (fnName.startsWith(text))
                    options.add(JSOption.from(this.scope.library.functions.get(fnName)));
            }
            return options;
        }

        Tuple<String, Type> fragment = lastTypeOfFragment(text);
        List<Property> filteredProperties = filter(fragment.second.getProperties(), prop -> prop.getName().equals(fragment.first));
        List<Method> filteredMethods = filter(fragment.second.getMethods(), method -> method.getName().startsWith(fragment.first));
        options.addAll(JSOption.fromProperties(filteredProperties));
        options.addAll(JSOption.fromFunctions(filteredMethods));

        return options;
    }

    private Tuple<String, Type> lastTypeOfFragment(String text)
    {
        String[] pieces = text.split("\\.");
        Type type = Type.ANY;
        if (pieces == null || pieces.length <= 0)
            return new Tuple<>(text, type);
        String last = pieces[pieces.length - 1];
        for (int i = 0; i < pieces.length; i++)
        {
            String piece = pieces[i];
            if (piece.matches(FN_PATTERN))
            {
                piece = piece.replaceAll("\\(\\)", "");
                Method method = methodWithName(piece, type.getMethods());
                if (method != null)
                    type = method.getReturnType();
            }
            else if (i == 0 && piece.matches(ID_PATTERN))
            {
                Variable variable = this.variableWithName(piece);
                if (variable != null)
                    type = variable.getType();
            }
            else if (piece.matches(ID_PATTERN))
            {
                Property property = propertyWithName(piece, type.getProperties());
                if (property != null)
                    type = property.getType();
            }
        }
        return new Tuple<>(last, type);
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

    private static Property propertyWithName(String name, List<Property> properties)
    {
        for (Property property : properties)
        {
            if (property.getName().equals(name))
                return property;
        }
        return null;
    }

    private static Method methodWithName(String name, List<Method> methods)
    {
        for (Method method : methods)
        {
            if (method.getName().equals(name))
                return method;
        }
        return null;
    }

    private Variable variableWithName(String name)
    {
        for (String key : this.scope.library.variables.keySet())
        {
            if (key.equals(name))
                return this.scope.library.variables.get(key);
        }
        return null;
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

    private static <T> List<T> filter(List<T> items, Function<T, Boolean> condition)
    {
        return items.stream().filter(condition::apply).collect(Collectors.toList());
    }
}


