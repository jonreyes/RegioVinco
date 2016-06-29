package rvme.gui;

import rvme.RegioVincoMapEditor;
import saf.AppTemplate;
import saf.components.AppWorkspaceComponent;

/**
 * This class serves as the workspace component for this application, providing
 * the user interface controls for editing work.
 *
 * @author Jon Reyes
 * @version 1.0
 */
public class Workspace extends AppWorkspaceComponent {
    AppTemplate app;
    
    public Workspace(AppTemplate initApp) {
        app = initApp;
    }

    @Override
    public void reloadWorkspace() {
    }

    @Override
    public void initStyle() {
    }

    
}
