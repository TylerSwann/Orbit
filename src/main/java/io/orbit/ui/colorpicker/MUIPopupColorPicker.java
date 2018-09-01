package io.orbit.ui.colorpicker;

import javafx.scene.Scene;
import javafx.stage.PopupWindow;

/**
 * Created by Tyler Swann on Friday August 31, 2018 at 19:06
 */
public class MUIPopupColorPicker extends PopupWindow
{

    public MUIPopupColorPicker()
    {
        MUIColorPicker colorPicker = new MUIColorPicker();
        this.getScene().setRoot(colorPicker);
    }
}
