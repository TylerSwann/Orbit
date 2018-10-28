package io.orbit.ui.notification.modal;

import javafx.scene.Node;
import javafx.scene.control.Skin;

/**
 * Created by Tyler Swann on Sunday July 15, 2018 at 19:18
 */
public class MUIModalSkin implements Skin<MUIModal>
{
    private MUIModal modal;
    public MUIModalSkin(MUIModal modal)
    {
        this.modal = modal;
    }

    @Override
    public MUIModal getSkinnable()
    {
        return this.modal;
    }

    @Override
    public Node getNode()
    {
        return this.modal.getScene().getRoot();
    }

    @Override
    public void dispose()
    {
        this.modal = null;
    }
}
