package io.orbit.controllers;

import io.orbit.ui.navigator.FileTreeMenuEvent;
import io.orbit.ui.navigator.MUIFileTreeView;

/**
 * Created by Tyler Swann on Saturday July 21, 2018 at 15:39
 */
public class OProjectViewController
{
    private MUIFileTreeView projectView;
    
    public OProjectViewController(MUIFileTreeView projectView)
    {
        this.projectView = projectView;
        this.registerListeners();
    }
    
    private void registerListeners()
    {
        this.projectView.addEventHandler(FileTreeMenuEvent.CUT, __-> {});
        this.projectView.addEventHandler(FileTreeMenuEvent.COPY, __-> {});
        this.projectView.addEventHandler(FileTreeMenuEvent.COPY_PATH, __-> {});
        this.projectView.addEventHandler(FileTreeMenuEvent.COPY_RELATIVE_PATH, __-> {});
        this.projectView.addEventHandler(FileTreeMenuEvent.PASTE, __-> {});
        this.projectView.addEventHandler(FileTreeMenuEvent.DELETE, __-> {});
        this.projectView.addEventHandler(FileTreeMenuEvent.NEW_FILE, __-> {});
        this.projectView.addEventHandler(FileTreeMenuEvent.NEW_FOLDER, __-> {});
        this.projectView.addEventHandler(FileTreeMenuEvent.NEW_PROJECT, __-> {});
    }
}
