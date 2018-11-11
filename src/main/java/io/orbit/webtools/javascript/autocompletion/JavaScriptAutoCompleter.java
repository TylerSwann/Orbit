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
import io.orbit.webtools.javascript.typedefs.parsing.Type;
import javafx.scene.input.KeyEvent;

/**
 * Created By: Tyler Swann.
 * Date: Thursday, Nov 08, 2018
 * Time: 3:12 PM
 * Website: https://orbiteditor.com
 */
public class JavaScriptAutoCompleter
{
    private AutoCompletionDialog<JSOption> dialog;
    private ProjectScope scope;
    private CodeEditor editor;

    public JavaScriptAutoCompleter(CodeEditor editor, ProjectScope scope)
    {
        this.editor = editor;
        this.scope = scope;
        this.dialog = new AutoCompletionDialog<>(editor);
        this.dialog.setCellFactory(JSOption::getDisplayText);
        this.registerListeners();
    }

    private void registerListeners()
    {
        this.editor.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            switch (event.getCode())
            {
                case ENTER: break;
                case BACK_SPACE:
                case COLON:
                case END:
                case HOME:
                    this.dialog.hide();
                    break;
                default:
                    this.keyWasReleased(event);
                    break;
            }
        });
    }

    private void keyWasReleased(KeyEvent event)
    {

    }
}